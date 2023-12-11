package com.programistich.kmmspm

import co.touchlab.kermit.Logger
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform {
    Logger.i { "Hello World Test adfasdfsff" }
    return IOSPlatform()
}
