package com.acme_industries.acmecaf.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapter
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapterItem
import com.acme_industries.acmecaf.ui.vouchers.VoucherRecyclerAdapter
import com.acme_industries.acmecaf.ui.vouchers.VoucherRecyclerAdapterItem
import org.json.JSONObject

class MainViewModel : ViewModel(), OrdersRecyclerAdapter.ItemClickListener,  VoucherRecyclerAdapter.VoucherClickListener {

    val cart = Cart()
    val products = ArrayList<Product>()
    val vouchers = ArrayList<Voucher>()
    val itemsLiveData = MutableLiveData<List<OrdersRecyclerAdapterItem>>()
    val vouchersLiveData = MutableLiveData<List<VoucherRecyclerAdapterItem>>()


    fun productMessageParse (response: JSONObject){
        val prodList = response.getJSONArray("Products")
        println(prodList)
        for (it in 0 until prodList.length()){
            val prod = prodList.getJSONObject(it)
            this.products.add(
                Product(prod.getInt("productid"),
                    prod.getString("title"),
                    prod.getString("details"),
                    prod.getDouble("price"),
                    prod.getString("image"))
            )
        }
        updateItemsLiveData()

        val vouchList = response.getJSONArray("Vouchers")
        println(prodList)
        for (it in 0 until vouchList.length()){
            val vouch = vouchList.getJSONObject(it)
            this.vouchers.add(
                Voucher(vouch.getString("title"),
                    vouch.getString("details"),
                    vouch.getString("image"))
            )
        }
        updateVoucherLiveData()
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

    private fun vouchers(): List<VoucherRecyclerAdapterItem> {
        return vouchers.map { voucher ->
            val title = voucher.title
            val details = voucher.details
            val image = voucher.image
            val quantity = 1
            val use = false
            VoucherRecyclerAdapterItem(title, details, image, quantity, use)
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

    private fun updateVoucherLiveData() {
        vouchersLiveData.postValue(vouchers())
    }

    fun getFormatedData(): String {

        //TODO Simplify order data, too much unnecessary info for order
        val data = JSONObject()
        data.put("Order",cart.orderList.map { (it.product.id.toString() + ":" + it.quantity.toString()).toString() })
        data.put("Total", cart.totalCost)

        return data.toString()
    }

    override fun checkVouch(vouchName: String) {
        TODO("Not yet implemented")
    }

    override fun uncheckVouch(vouchName: String) {
        TODO("Not yet implemented")
    }
}