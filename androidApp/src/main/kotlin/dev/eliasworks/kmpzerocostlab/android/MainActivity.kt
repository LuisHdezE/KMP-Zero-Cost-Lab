package dev.eliasworks.kmpzerocostlab.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.eliasworks.kmpzerocostlab.App
import dev.eliasworks.kmpzerocostlab.data.local.createDatabase

class MainActivity : ComponentActivity() {
    private val database by lazy {
        createDatabase(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App(database) }
    }
}
