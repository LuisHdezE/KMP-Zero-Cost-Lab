package dev.eliasworks.kmpzerocostlab.data

import dev.eliasworks.kmpzerocostlab.data.local.ProductDao
import dev.eliasworks.kmpzerocostlab.data.local.ProductEntity
import dev.eliasworks.kmpzerocostlab.domain.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RoomProductRepositoryTest {
    @Test
    fun observeAllMapsEntitiesToDomain() = runTest {
        val dao = FakeProductDao()
        val repository = RoomProductRepository(dao)
        dao.rows.value = listOf(
            ProductEntity(id = 9, name = "Mapped", quantity = 4, priceCents = 999)
        )

        assertEquals(
            listOf(Product(id = 9, name = "Mapped", quantity = 4, priceCents = 999)),
            repository.observeAll().first(),
        )
    }

    @Test
    fun createTrimsNameBeforeInsert() = runTest {
        val dao = FakeProductDao()
        val repository = RoomProductRepository(dao)

        repository.create("  Alpha  ", 2, 250)

        assertEquals(
            ProductEntity(name = "Alpha", quantity = 2, priceCents = 250),
            dao.inserted.single(),
        )
    }

    @Test
    fun updateMapsDomainToEntity() = runTest {
        val dao = FakeProductDao()
        val repository = RoomProductRepository(dao)

        repository.update(Product(id = 5, name = "Beta", quantity = 3, priceCents = 700))

        assertEquals(
            ProductEntity(id = 5, name = "Beta", quantity = 3, priceCents = 700),
            dao.updated.single(),
        )
    }

    @Test
    fun deleteForwardsIdentifier() = runTest {
        val dao = FakeProductDao()
        val repository = RoomProductRepository(dao)

        repository.deleteById(77)

        assertEquals(listOf(77L), dao.deletedIds)
    }
}

private class FakeProductDao : ProductDao {
    val rows = MutableStateFlow<List<ProductEntity>>(emptyList())
    val inserted = mutableListOf<ProductEntity>()
    val updated = mutableListOf<ProductEntity>()
    val deletedIds = mutableListOf<Long>()

    override fun observeAll(): Flow<List<ProductEntity>> = rows

    override suspend fun insert(product: ProductEntity): Long {
        inserted += product
        return product.id
    }

    override suspend fun update(product: ProductEntity) {
        updated += product
    }

    override suspend fun deleteById(id: Long) {
        deletedIds += id
    }
}
