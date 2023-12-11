package com.programistich.spm.plugin.models

enum class BuildType {
    DEBUG, RELEASE;

    fun getPath(): String {
        return when (this) {
            DEBUG -> "debug"
            RELEASE -> "release"
        }
    }

    companion object {
        fun fromString(raw: String?): BuildType? {
            return when (raw?.lowercase()) {
                "debug" -> DEBUG
                "release" -> RELEASE
                else -> null
            }
        }
    }
}