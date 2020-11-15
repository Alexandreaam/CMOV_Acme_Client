package com.acme_industries.acmecaf.core

class Cart {
    val orderList: ArrayList<Order> = ArrayList()
    val totalCost: Double
        get(){
            return orderList.map { it.totalCost }.sum()
        }

    fun addOrder (order : Order) {
        orderList.add(order)
    }
    fun removeOrder (order: Order) {
        orderList.remove(order)
    }
}