package dev.eliasworks.kmpzerocostlab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.eliasworks.kmpzerocostlab.data.RoomProductRepository
import dev.eliasworks.kmpzerocostlab.data.local.AppDatabase
import dev.eliasworks.kmpzerocostlab.domain.Product
import kotlinx.coroutines.launch

private enum class LabSection {
    PRODUCTS,
    EDITOR,
}

@Composable
fun App(database: AppDatabase) {
    MaterialTheme {
        val repository = remember(database) { RoomProductRepository(database.productDao()) }
        val products by repository.observeAll().collectAsState(initial = emptyList())
        val scope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current

        var selectedSection by remember { mutableStateOf(LabSection.PRODUCTS) }
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

        fun showProducts() {
            focusManager.clearFocus(force = true)
            selectedSection = LabSection.PRODUCTS
        }

        fun showEditor() {
            focusManager.clearFocus(force = true)
            selectedSection = LabSection.EDITOR
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(LabMarker.name)
                            Text(
                                text = "Room + SQLite · ${platformName()}",
                                style = MaterialTheme.typography.caption,
                            )
                        }
                    },
                    elevation = 0.dp,
                )
            },
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding),
            ) {
                TabRow(selectedTabIndex = if (selectedSection == LabSection.PRODUCTS) 0 else 1) {
                    Tab(
                        selected = selectedSection == LabSection.PRODUCTS,
                        onClick = ::showProducts,
                        text = { Text("Products (${products.size})") },
                    )
                    Tab(
                        selected = selectedSection == LabSection.EDITOR,
                        onClick = {
                            if (editingId == null) clearForm()
                            showEditor()
                        },
                        text = { Text(if (editingId == null) "New" else "Edit") },
                    )
                }

                when (selectedSection) {
                    LabSection.PRODUCTS -> ProductsScreen(
                        products = products,
                        onAdd = {
                            clearForm()
                            showEditor()
                        },
                        onEdit = { product ->
                            editingId = product.id
                            name = product.name
                            quantity = product.quantity.toString()
                            priceCents = product.priceCents.toString()
                            showEditor()
                        },
                        onDelete = { product ->
                            scope.launch {
                                repository.delete(product)
                                if (editingId == product.id) clearForm()
                            }
                        },
                    )

                    LabSection.EDITOR -> ProductEditorScreen(
                        editing = editingId != null,
                        name = name,
                        quantity = quantity,
                        priceCents = priceCents,
                        onNameChange = { name = it },
                        onQuantityChange = { quantity = it.filter(Char::isDigit) },
                        onPriceChange = { priceCents = it.filter(Char::isDigit) },
                        onCancel = {
                            clearForm()
                            showProducts()
                        },
                        onSave = {
                            val safeQuantity = quantity.toIntOrNull() ?: 0
                            val safePrice = priceCents.toLongOrNull() ?: 0L
                            val id = editingId

                            focusManager.clearFocus(force = true)
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
                                selectedSection = LabSection.PRODUCTS
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductsScreen(
    products: List<Product>,
    onAdd: () -> Unit,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("Products", style = MaterialTheme.typography.h6)
                Text(
                    text = "${products.size} stored locally",
                    style = MaterialTheme.typography.caption,
                )
            }
            Button(onClick = onAdd) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(8.dp))
        Divider()

        if (products.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("No products yet", style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(8.dp))
                Text("Add one to validate Room + SQLite persistence.")
                Spacer(Modifier.height(16.dp))
                Button(onClick = onAdd) {
                    Text("Create product")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(products, key = { it.id }) { product ->
                    ProductRow(
                        product = product,
                        onEdit = { onEdit(product) },
                        onDelete = { onDelete(product) },
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun ProductEditorScreen(
    editing: Boolean,
    name: String,
    quantity: String,
    priceCents: String,
    onNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp),
    ) {
        Text(
            text = if (editing) "Edit product" else "New product",
            style = MaterialTheme.typography.h6,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Stored locally with Room + SQLite",
            style = MaterialTheme.typography.caption,
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Product name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = quantity,
            onValueChange = onQuantityChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Quantity") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = priceCents,
            onValueChange = onPriceChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Price in cents") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(force = true) },
            ),
        )

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                enabled = name.isNotBlank(),
                onClick = onSave,
            ) {
                Text(if (editing) "Save changes" else "Create product")
            }

            Button(onClick = onCancel) {
                Text("Cancel")
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
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(product.name, style = MaterialTheme.typography.subtitle1)
        Text(
            text = "Qty: ${product.quantity} · Price: ${product.priceCents} cents",
            style = MaterialTheme.typography.body2,
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onEdit) { Text("Edit") }
            Button(onClick = onDelete) { Text("Delete") }
        }
    }
}
