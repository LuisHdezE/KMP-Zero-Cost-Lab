package dev.eliasworks.kmpzerocostlab

import platform.UIKit.UIDevice

actual fun platformName(): String {
    val device = UIDevice.currentDevice
    return "${device.systemName} ${device.systemVersion}"
}
