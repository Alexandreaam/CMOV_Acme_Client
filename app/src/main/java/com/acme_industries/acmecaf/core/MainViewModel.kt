package com.acme_industries.acmecaf.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapter
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapterItem
import org.json.JSONObject

class MainViewModel : ViewModel(), OrdersRecyclerAdapter.ItemClickListener {

    val cart = Cart()
    val products = ArrayList<Product>()
    val vouchers = ArrayList<Voucher>()
    val itemsLiveData = MutableLiveData<List<OrdersRecyclerAdapterItem>>()

    fun productMessageParse (response: JSONObject){
        val prodList = response.getJSONArray("Products")
        println(prodList)
        for (it in 0 until prodList.length()){
            val prod = prodList.getJSONObject(it)
            this.products.add(
                Product(prod.getString("title"),
                    prod.getString("details"),
                    prod.getDouble("price"),
                    prod.getString("image"))
            )
        }
        updateItemsLiveData()
    }

    fun voucherMessageParse (response: JSONObject){
        val prodList = response.getJSONArray("Vouchers")
        println(prodList)
        for (it in 0 until prodList.length()){
            val prod = prodList.getJSONObject(it)
            this.vouchers.add(
                Voucher(prod.getString("title"),
                    prod.getString("details"),
                    prod.getString("image"))
            )
        }
        //updateItemsLiveData()
    }

    private fun items(): List<OrdersRecyclerAdapterItem> {
        return products.map { product ->
            val title = product.title
            val details = product.details
            val price = product.price
            val image = product.image
            val quantity = cart.orderList.find { it.product == product }?.quantity ?: 0
            OrdersRecyclerAdapterItem(title, details, price, image, quantity)
        }
    }


    override fun incrementQuantity(productName: String) {
        if (!cart.orderList.any { it.product.title == productName }) {
            cart.addOrder(Order(products.first { it.title == productName }, 1))
        } else {
            cart.orderList.first { it.product.title == productName }.quantity += 1
        }
        updateItemsLiveData()
    }

    override fun decreaseQuantity(productName: String) {
        if (cart.orderList.any { it.product.title == productName }) {
            val order = cart.orderList.first { it.product.title == productName }
            order.quantity -= 1
            if (order.quantity == 0)
                cart.removeOrder(order)
            updateItemsLiveData()
        }
    }

    private fun updateItemsLiveData() {
        itemsLiveData.postValue(items())
    }
}