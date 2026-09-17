package dev.eliasworks.kmpzerocostlab

import android.content.Context
import dev.eliasworks.kmpzerocostlab.data.RoomProductRepository
import dev.eliasworks.kmpzerocostlab.data.local.createDatabase

fun createAndroidProductController(context: Context): ProductController {
    val database = createDatabase(context.applicationContext)
    return ProductController(
        repository = RoomProductRepository(database.productDao())
    )
}
