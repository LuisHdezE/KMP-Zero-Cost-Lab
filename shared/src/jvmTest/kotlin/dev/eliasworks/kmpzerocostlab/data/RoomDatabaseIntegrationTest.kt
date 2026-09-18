package dev.eliasworks.kmpzerocostlab.data

import androidx.room3.Room
import dev.eliasworks.kmpzerocostlab.data.local.AppDatabase
import dev.eliasworks.kmpzerocostlab.data.local.buildDatabase
import dev.eliasworks.kmpzerocostlab.domain.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.nio.file.Files
import kotlin.io.path.absolutePathString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoomDatabaseIntegrationTest {
    @Test
    fun crudAndCloseReopenPreserveData() = runBlocking {
        val directory = Files.createTempDirectory("kmp-room-qa-")
        val databasePath = directory.resolve("products.db").absolutePathString()
        var database = openDatabase(databasePath)

        try {
            var repository = RoomProductRepository(database.productDao())
            assertTrue(repository.observeAll().first().isEmpty())

            repository.create("Alpha", 1, 100)
            repository.create("Beta", 2, 250)

            val created = awaitProducts(repository, expectedCount = 2)
            assertEquals(listOf("Beta", "Alpha"), created.map(Product::name))

            val beta = created.first { it.name == "Beta" }
            repository.update(beta.copy(name = "Beta updated", quantity = 5, priceCents = 900))

            val updated = withTimeout(5_000) {
                repository.observeAll().first { products ->
                    products.any { it.id == beta.id && it.name == "Beta updated" }
                }
            }
            assertEquals(5, updated.first { it.id == beta.id }.quantity)

            val alpha = updated.first { it.name == "Alpha" }
            repository.deleteById(alpha.id)

            val afterDelete = awaitProducts(repository, expectedCount = 1)
            assertEquals("Beta updated", afterDelete.single().name)

            database.close()
            database = openDatabase(databasePath)
            repository = RoomProductRepository(database.productDao())

            val reopened = awaitProducts(repository, expectedCount = 1)
            assertEquals(
                Product(
                    id = beta.id,
                    name = "Beta updated",
                    quantity = 5,
                    priceCents = 900,
                ),
                reopened.single(),
            )
        } finally {
            database.close()
            directory.toFile().deleteRecursively()
        }
    }

    private fun openDatabase(path: String): AppDatabase =
        buildDatabase(
            Room.databaseBuilder<AppDatabase>(
                name = path,
            )
        )

    private suspend fun awaitProducts(
        repository: RoomProductRepository,
        expectedCount: Int,
    ): List<Product> = withTimeout(5_000) {
        repository.observeAll().first { it.size == expectedCount }
    }
}
