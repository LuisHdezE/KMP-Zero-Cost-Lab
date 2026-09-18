package dev.eliasworks.kmpzerocostlab

import dev.eliasworks.kmpzerocostlab.domain.Product
import dev.eliasworks.kmpzerocostlab.domain.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductSnapshot internal constructor(
    private val items: List<Product>,
) {
    val count: Int
        get() = items.size

    fun productAt(index: Int): Product = items[index]

    fun asList(): List<Product> = items
}

class ProductController internal constructor(
    private val repository: ProductRepository,
    private val scope: CoroutineScope,
    private val cancelScopeOnDispose: Boolean,
) {
    constructor(repository: ProductRepository) : this(
        repository = repository,
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
        cancelScopeOnDispose = true,
    )

    internal constructor(
        repository: ProductRepository,
        scope: CoroutineScope,
    ) : this(
        repository = repository,
        scope = scope,
        cancelScopeOnDispose = false,
    )

    private var observationJob: Job? = null

    fun start(onChange: (ProductSnapshot) -> Unit) {
        observationJob?.cancel()
        observationJob = scope.launch {
            repository.observeAll().collectLatest { products ->
                onChange(ProductSnapshot(products))
            }
        }
    }

    fun stop() {
        observationJob?.cancel()
        observationJob = null
    }

    fun createProduct(name: String, quantity: Int, priceCents: Long) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return

        scope.launch {
            repository.create(
                name = cleanName,
                quantity = quantity.coerceAtLeast(0),
                priceCents = priceCents.coerceAtLeast(0),
            )
        }
    }

    fun updateProduct(id: Long, name: String, quantity: Int, priceCents: Long) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return

        scope.launch {
            repository.update(
                Product(
                    id = id,
                    name = cleanName,
                    quantity = quantity.coerceAtLeast(0),
                    priceCents = priceCents.coerceAtLeast(0),
                )
            )
        }
    }

    fun deleteProduct(id: Long) {
        scope.launch {
            repository.deleteById(id)
        }
    }

    fun dispose() {
        stop()
        if (cancelScopeOnDispose) {
            scope.cancel()
        }
    }
}
