package com.programistich.kmmspm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform