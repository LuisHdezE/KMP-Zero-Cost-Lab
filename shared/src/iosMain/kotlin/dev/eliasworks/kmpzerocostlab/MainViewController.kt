package dev.eliasworks.kmpzerocostlab

import androidx.compose.ui.window.ComposeUIViewController
import dev.eliasworks.kmpzerocostlab.data.local.buildDatabase
import dev.eliasworks.kmpzerocostlab.data.local.getDatabaseBuilder

fun MainViewController(): platform.UIKit.UIViewController {
    val database = buildDatabase(getDatabaseBuilder())
    return ComposeUIViewController { App(database) }
}
