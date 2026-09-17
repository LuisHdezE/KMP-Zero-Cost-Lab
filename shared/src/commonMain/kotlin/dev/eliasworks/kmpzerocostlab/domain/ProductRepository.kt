package dev.eliasworks.kmpzerocostlab.domain

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeAll(): Flow<List<Product>>
    suspend fun create(name: String, quantity: Int, priceCents: Long)
    suspend fun update(product: Product)
    suspend fun deleteById(id: Long)
}
