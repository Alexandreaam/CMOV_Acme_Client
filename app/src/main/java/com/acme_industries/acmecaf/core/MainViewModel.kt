package com.acme_industries.acmecaf.core

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapter
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapterItem
import com.acme_industries.acmecaf.ui.vouchers.VoucherRecyclerAdapter
import com.acme_industries.acmecaf.ui.vouchers.VoucherRecyclerAdapterItem
import org.json.JSONObject

class MainViewModel : ViewModel(), OrdersRecyclerAdapter.ItemClickListener,  VoucherRecyclerAdapter.VoucherClickListener {

    var cart = Cart()
    private var products = ArrayList<Product>()
    private var vouchers = ArrayList<Voucher>()
    var userStats: UserStats? = null
    var itemsLiveData = MutableLiveData<List<OrdersRecyclerAdapterItem>>()
    var vouchersLiveData = MutableLiveData<List<VoucherRecyclerAdapterItem>>()
    var has5disc = false

    fun reset() {
        cart = Cart()
        products.clear()
        vouchers.clear()
        userStats = null
        has5disc = false
        updateItemsLiveData()
        updateVoucherLiveData()
    }

    fun productMessageParse (response: JSONObject){
        val prodList = response.getJSONArray("Products")
        println(prodList)
        this.products.clear()
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
        this.vouchers.clear()
        for (it in 0 until vouchList.length()){
            val vouch = vouchList.getJSONObject(it)
            this.vouchers.add(
                Voucher(vouch.getString("vouchid"),
                    vouch.getString("title"),
                    vouch.getString("details"),
                    vouch.getString("image"),
                    vouch.getBoolean("type"))
            )
        }
        updateVoucherLiveData()

        val dataList = response.getJSONArray("Userdata")
        println(prodList)
        this.userStats = null
        for (it in 0 until dataList.length()){
            val data = dataList.getJSONObject(it)
            this.userStats = UserStats(data.getInt("coffeecount"),
                data.getDouble("totalspendings"),
                data.getInt("tempcoffeecount"),
                data.getDouble("tempspendings"))
        }
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
            val id = voucher.id
            val title = voucher.title
            val details = voucher.details
            val image = voucher.image
            val type = voucher.type
            val use = cart.orderVoucherList.find { it.voucher.id == voucher.id }?.use ?: false
            VoucherRecyclerAdapterItem(id, title, details, image, use, type)
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

    fun getFormatedData(userid: String): String {

        //TODO Simplify order data, too much unnecessary info for order
        val data = JSONObject()
        data.put("Products",cart.orderList.map { ("\"" + it.product.id.toString() + "\":" + it.quantity.toString()) }.toString().replace("[", "{").replace("]", "}"))
        data.put("Vouchers",cart.orderVoucherList.map { ("\"" + it.voucher.id + "\":" + it.voucher.type.toString()) }.toString().replace("[", "{").replace("]", "}"))
        data.put("userid", userid)
        data.put("Total", cart.totalCost)

        return data.toString()
    }

    override fun checkVouch(vouchId: String) {
        if (!cart.orderVoucherList.any { it.voucher.id == vouchId }) {
            cart.addVoucher(OrderVoucher(vouchers.first { it.id == vouchId}, true))
        } else {
            val orderVoucher = cart.orderVoucherList.first { it.voucher.id == vouchId }
            cart.removeVoucher(orderVoucher)
        }
        has5disc = cart.orderVoucherList.any { !it.voucher.type }
        updateVoucherLiveData()
    }
}