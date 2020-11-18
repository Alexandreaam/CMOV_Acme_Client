package com.acme_industries.acmecaf.core

class Cart {
    val orderList: ArrayList<Order> = ArrayList()
    val orderVoucherList: ArrayList<OrderVoucher> = ArrayList()
    val totalCost: Double
        //TODO (Make voucher deduct from price)
        get(){
            return orderList.map { it.totalCost }.sum()
        }

    fun addOrder (order : Order) {
        orderList.add(order)
    }
    fun removeOrder (order: Order) {
        orderList.remove(order)
    }

    fun addVoucher(voucher: OrderVoucher) {
        orderVoucherList.add(voucher)
    }

    fun removeVoucher(voucher: OrderVoucher) {
        orderVoucherList.remove(voucher)
    }
}