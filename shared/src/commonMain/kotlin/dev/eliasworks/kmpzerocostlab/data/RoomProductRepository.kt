package dev.eliasworks.kmpzerocostlab.data

import dev.eliasworks.kmpzerocostlab.data.local.ProductDao
import dev.eliasworks.kmpzerocostlab.data.local.ProductEntity
import dev.eliasworks.kmpzerocostlab.domain.Product
import dev.eliasworks.kmpzerocostlab.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomProductRepository(
    private val dao: ProductDao,
) : ProductRepository {
    override fun observeAll(): Flow<List<Product>> =
        dao.observeAll().map { rows -> rows.map(ProductEntity::toDomain) }

    override suspend fun create(name: String, quantity: Int, priceCents: Long) {
        dao.insert(
            ProductEntity(
                name = name.trim(),
                quantity = quantity,
                priceCents = priceCents,
            )
        )
    }

    override suspend fun update(product: Product) {
        dao.update(product.toEntity())
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

private fun ProductEntity.toDomain() = Product(
    id = id,
    name = name,
    quantity = quantity,
    priceCents = priceCents,
)

private fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    quantity = quantity,
    priceCents = priceCents,
)
