package dev.eliasworks.kmpzerocostlab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        var count by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(LabMarker.name, style = MaterialTheme.typography.h5)
            Spacer(Modifier.height(12.dp))
            Text(LabMarker.phase)
            Text("Platform: ${platformName()}")
            Spacer(Modifier.height(24.dp))
            Text("Shared counter: $count")
            Spacer(Modifier.height(12.dp))
            Button(onClick = { count++ }) {
                Text("+1")
            }
        }
    }
}
