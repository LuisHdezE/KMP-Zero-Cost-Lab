package dev.eliasworks.kmpzerocostlab

actual fun platformName(): String = "JVM ${System.getProperty("java.version")}"
