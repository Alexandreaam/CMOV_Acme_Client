package com.acme_industries.acmecaf.core

data class Order (val product: Product, var quantity: Int){
    val totalCost: Double
        get() = product.price * quantity
}