package dev.eliasworks.kmpzerocostlab.domain

data class Product(
    val id: Long = 0,
    val name: String,
    val quantity: Int,
    val priceCents: Long,
)
