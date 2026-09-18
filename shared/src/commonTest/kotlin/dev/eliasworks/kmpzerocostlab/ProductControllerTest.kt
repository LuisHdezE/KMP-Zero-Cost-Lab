package dev.eliasworks.kmpzerocostlab

import dev.eliasworks.kmpzerocostlab.domain.Product
import dev.eliasworks.kmpzerocostlab.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductControllerTest {
    @Test
    fun createTrimsNameAndClampsNegativeNumbers() = runTest {
        val repository = FakeProductRepository()
        val controller = ProductController(repository, backgroundScope)

        controller.createProduct("  Widget  ", -4, -100)
        runCurrent()

        assertEquals(listOf(CreateCall("Widget", 0, 0)), repository.createCalls)
    }

    @Test
    fun createWithBlankNameIsIgnored() = runTest {
        val repository = FakeProductRepository()
        val controller = ProductController(repository, backgroundScope)

        controller.createProduct("   ", 2, 300)
        runCurrent()

        assertTrue(repository.createCalls.isEmpty())
    }

    @Test
    fun updateTrimsNameAndClampsNegativeNumbers() = runTest {
        val repository = FakeProductRepository()
        val controller = ProductController(repository, backgroundScope)

        controller.updateProduct(7, "  Updated  ", -1, -2)
        runCurrent()

        assertEquals(
            listOf(Product(id = 7, name = "Updated", quantity = 0, priceCents = 0)),
            repository.updateCalls,
        )
    }

    @Test
    fun updateWithBlankNameIsIgnored() = runTest {
        val repository = FakeProductRepository()
        val controller = ProductController(repository, backgroundScope)

        controller.updateProduct(7, " \t ", 1, 100)
        runCurrent()

        assertTrue(repository.updateCalls.isEmpty())
    }

    @Test
    fun deleteIsForwardedToRepository() = runTest {
        val repository = FakeProductRepository()
        val controller = ProductController(repository, backgroundScope)

        controller.deleteProduct(42)
        runCurrent()

        assertEquals(listOf(42L), repository.deleteCalls)
    }

    @Test
    fun startPublishesSnapshotsAndStopEndsObservation() = runTest {
        val repository = FakeProductRepository()
        val controller = ProductController(repository, backgroundScope)
        val snapshots = mutableListOf<ProductSnapshot>()

        controller.start(snapshots::add)
        runCurrent()

        assertEquals(1, snapshots.size)
        assertEquals(0, snapshots.single().count)

        val product = Product(id = 3, name = "Observed", quantity = 2, priceCents = 450)
        repository.emit(listOf(product))
        runCurrent()

        assertEquals(2, snapshots.size)
        assertEquals(1, snapshots.last().count)
        assertEquals(product, snapshots.last().productAt(0))
        assertEquals(listOf(product), snapshots.last().asList())

        controller.stop()
        repository.emit(emptyList())
        runCurrent()

        assertEquals(2, snapshots.size)
    }

    @Test
    fun startingAgainReplacesPreviousObserver() = runTest {
        val repository = FakeProductRepository()
        val controller = ProductController(repository, backgroundScope)
        var firstObserverCalls = 0
        var secondObserverCalls = 0

        controller.start { firstObserverCalls++ }
        runCurrent()
        controller.start { secondObserverCalls++ }
        runCurrent()

        repository.emit(
            listOf(Product(id = 1, name = "Only second", quantity = 1, priceCents = 1))
        )
        runCurrent()

        assertEquals(1, firstObserverCalls)
        assertEquals(2, secondObserverCalls)
    }
}

private data class CreateCall(
    val name: String,
    val quantity: Int,
    val priceCents: Long,
)

private class FakeProductRepository : ProductRepository {
    private val products = MutableStateFlow<List<Product>>(emptyList())

    val createCalls = mutableListOf<CreateCall>()
    val updateCalls = mutableListOf<Product>()
    val deleteCalls = mutableListOf<Long>()

    override fun observeAll(): Flow<List<Product>> = products

    override suspend fun create(name: String, quantity: Int, priceCents: Long) {
        createCalls += CreateCall(name, quantity, priceCents)
    }

    override suspend fun update(product: Product) {
        updateCalls += product
    }

    override suspend fun deleteById(id: Long) {
        deleteCalls += id
    }

    fun emit(value: List<Product>) {
        products.value = value
    }
}
