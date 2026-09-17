package dev.eliasworks.kmpzerocostlab

import dev.eliasworks.kmpzerocostlab.data.RoomProductRepository
import dev.eliasworks.kmpzerocostlab.data.local.buildDatabase
import dev.eliasworks.kmpzerocostlab.data.local.getDatabaseBuilder

fun createIosProductController(): ProductController {
    val database = buildDatabase(getDatabaseBuilder())
    return ProductController(
        repository = RoomProductRepository(database.productDao())
    )
}
