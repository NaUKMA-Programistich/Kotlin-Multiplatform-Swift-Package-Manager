package com.programistich.spm.plugin.utils

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

private const val BUFFER_SIZE = 4096

fun File.createFolderIfNotExist() {
    if (!exists()) {
        mkdirs()
    }
}

fun File.createFileIfNotExist() {
    if (!exists()) {
        createNewFile()
    }
}

fun File.unzip(destDirectory: String) {
    File(destDirectory).mkdirs()

    ZipFile(this).use { zip ->
        zip.entries().asSequence().forEach { entry ->
            val entryPathSegments = entry.name.split(File.separator).drop(1)
            processZipEntry(entryPathSegments, destDirectory, entry, zip)
        }
    }
}

private fun processZipEntry(
    entryPathSegments: List<String>,
    destDirectory: String,
    entry: ZipEntry,
    zip: ZipFile
) {
    if (entryPathSegments.isNotEmpty()) {
        val filePath =
            destDirectory + File.separator + entryPathSegments.joinToString(File.separator)
        if (!entry.isDirectory) {
            zip.getInputStream(entry).use { input ->
                extractFile(input, filePath)
            }
        } else {
            File(filePath).mkdirs()
        }
    }
}

private fun extractFile(inputStream: InputStream, destFilePath: String) {
    BufferedOutputStream(FileOutputStream(destFilePath)).use { bos ->
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
    }
}
