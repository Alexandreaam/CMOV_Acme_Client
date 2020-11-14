package com.acme_industries.acmecaf.core

class Cart {
    private val productList: ArrayList<Product> = ArrayList()
    val totalCost: Double
        get(){
            return productList.map { it.quantity * it.price }.sum()
        }

    fun addProduct (product : Product) {
        productList.add(product)
    }
    fun removeProduct (product: Product) {
        productList.remove(product)
    }
    fun removeProduct (product: String) {
        productList.remove(productList.first { it.title == product })
    }

    fun getTotalOf (name : String): Double {
        return productList.filter { it.title == name }
                            .map { it.quantity * it.price }
                            .sum()
    }
}