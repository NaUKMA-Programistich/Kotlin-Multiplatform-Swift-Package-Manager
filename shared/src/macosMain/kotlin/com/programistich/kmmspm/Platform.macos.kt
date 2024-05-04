package com.programistich.kmmspm

import platform.Foundation.NSProcessInfo

class MacOSPlatform: Platform {
    override val name: String = "macOS " + NSProcessInfo.processInfo.operatingSystemVersionString
}

actual fun getPlatform(): Platform {
    return MacOSPlatform()
}
