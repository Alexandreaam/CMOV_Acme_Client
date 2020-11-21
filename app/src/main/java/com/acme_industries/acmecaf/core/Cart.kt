package com.acme_industries.acmecaf.core

class Cart {
    val orderList: ArrayList<Order> = ArrayList()
    val orderVoucherList: ArrayList<OrderVoucher> = ArrayList()
    val totalCost: Double
        get(){
            var disc = 0
            var coffeeDisc = 0.0
            orderVoucherList.map {
                if(it.voucher.type) {
                    disc++
                }
            }
            orderList.map {
                if(it.product.id == 4) {
                    coffeeDisc = if(it.quantity >= disc)
                        0.50 * disc
                    else
                        0.50 * it.quantity
                }
            }
            var total = orderList.map { it.totalCost }.sum() - coffeeDisc
            if(orderVoucherList.any { !it.voucher.type }){
                total *= 0.95
            }
            return total
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