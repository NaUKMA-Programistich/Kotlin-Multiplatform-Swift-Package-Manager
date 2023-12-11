package com.programistich.kmmspm

import co.touchlab.kermit.Logger
import platform.Foundation.NSProcessInfo

class MacOSPlatform: Platform {
    override val name: String = "macOS " + NSProcessInfo.processInfo.operatingSystemVersionString
}

actual fun getPlatform(): Platform {
    Logger.i { "Hello World macos" }
    return MacOSPlatform()
}
