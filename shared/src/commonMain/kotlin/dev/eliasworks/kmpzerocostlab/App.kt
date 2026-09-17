package dev.eliasworks.kmpzerocostlab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.eliasworks.kmpzerocostlab.data.RoomProductRepository
import dev.eliasworks.kmpzerocostlab.data.local.AppDatabase
import dev.eliasworks.kmpzerocostlab.domain.Product
import kotlinx.coroutines.launch

@Composable
fun App(database: AppDatabase) {
    MaterialTheme {
        val repository = remember(database) { RoomProductRepository(database.productDao()) }
        val products by repository.observeAll().collectAsState(initial = emptyList())
        val scope = rememberCoroutineScope()

        var editingId by remember { mutableStateOf<Long?>(null) }
        var name by remember { mutableStateOf("") }
        var quantity by remember { mutableStateOf("1") }
        var priceCents by remember { mutableStateOf("100") }

        fun clearForm() {
            editingId = null
            name = ""
            quantity = "1"
            priceCents = "100"
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Text(LabMarker.name, style = MaterialTheme.typography.h5)
            Text(LabMarker.phase)
            Text("Platform: ${platformName()}")
            Text("Database: Room + SQLite")
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Product name") },
                singleLine = true,
            )
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it.filter(Char::isDigit) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Quantity") },
                singleLine = true,
            )
            OutlinedTextField(
                value = priceCents,
                onValueChange = { priceCents = it.filter(Char::isDigit) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Price in cents") },
                singleLine = true,
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    enabled = name.isNotBlank(),
                    onClick = {
                        val safeQuantity = quantity.toIntOrNull() ?: 0
                        val safePrice = priceCents.toLongOrNull() ?: 0L
                        val id = editingId
                        scope.launch {
                            if (id == null) {
                                repository.create(name, safeQuantity, safePrice)
                            } else {
                                repository.update(
                                    Product(
                                        id = id,
                                        name = name.trim(),
                                        quantity = safeQuantity,
                                        priceCents = safePrice,
                                    )
                                )
                            }
                            clearForm()
                        }
                    },
                ) {
                    Text(if (editingId == null) "Create" else "Save")
                }

                if (editingId != null) {
                    Button(onClick = ::clearForm) {
                        Text("Cancel")
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Text("Products: ${products.size}", style = MaterialTheme.typography.h6)
            Divider()

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(products, key = { it.id }) { product ->
                    ProductRow(
                        product = product,
                        onEdit = {
                            editingId = product.id
                            name = product.name
                            quantity = product.quantity.toString()
                            priceCents = product.priceCents.toString()
                        },
                        onDelete = {
                            scope.launch {
                                repository.delete(product)
                                if (editingId == product.id) clearForm()
                            }
                        },
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun ProductRow(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(product.name, style = MaterialTheme.typography.subtitle1)
        Text("Qty: ${product.quantity} | Price: ${product.priceCents} cents")
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onEdit) { Text("Edit") }
            Button(onClick = onDelete) { Text("Delete") }
        }
    }
}
