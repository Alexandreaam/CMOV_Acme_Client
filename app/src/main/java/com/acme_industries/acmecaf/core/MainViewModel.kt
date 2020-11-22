package com.acme_industries.acmecaf.core

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapter
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapterItem
import com.acme_industries.acmecaf.ui.user.UserRecyclerAdapter
import com.acme_industries.acmecaf.ui.user.UserRecyclerAdapterItem
import com.acme_industries.acmecaf.ui.vouchers.VoucherRecyclerAdapter
import com.acme_industries.acmecaf.ui.vouchers.VoucherRecyclerAdapterItem
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainViewModel : ViewModel(), OrdersRecyclerAdapter.ItemClickListener,  VoucherRecyclerAdapter.VoucherClickListener,  UserRecyclerAdapter.UserClickListener {

    var cart = Cart()
    private var products = ArrayList<Product>()
    private var vouchers = ArrayList<Voucher>()
    private var pastOrders = ArrayList<PastOrder>()
    var userStats: UserStats? = null
    var itemsLiveData = MutableLiveData<List<OrdersRecyclerAdapterItem>>()
    var vouchersLiveData = MutableLiveData<List<VoucherRecyclerAdapterItem>>()
    var pastOrdersLiveData = MutableLiveData<List<UserRecyclerAdapterItem>>()

    var has5disc = false

    fun reset() {
        cart = Cart()
        products.clear()
        vouchers.clear()
        pastOrders.clear()
        userStats = null
        has5disc = false
        updateItemsLiveData()
        updateVoucherLiveData()
        updateUserLiveData()
    }

    fun loadData(userid: String, context: Context) {
        val menuRequest = JSONObject()
        menuRequest.put("userid", userid)

        val url = Constants.serverUrl + "menu"

        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, menuRequest ,
            { response ->
                println("Response is: $response")
                this.productMessageParse(response)
            },
            { error ->
                println("That didn't work: $error")
            })
        queue.add(jsonObjectRequest)
    }

    fun productMessageParse (response: JSONObject){
        val prodList = response.getJSONArray("Products")
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
        this.userStats = null
        for (it in 0 until dataList.length()){
            val data = dataList.getJSONObject(it)
            this.userStats = UserStats(data.getInt("coffeecount"),
                data.getDouble("totalspendings"),
                data.getInt("tempcoffeecount"),
                data.getDouble("tempspendings"))
        }

        val pastOrderList = response.getJSONArray("Pastorders")
        this.pastOrders.clear()
        for (it in 0 until pastOrderList.length()){
            val pastOrder = pastOrderList.getJSONObject(it)
            this.pastOrders.add(
                PastOrder(pastOrder.getInt("orderid"),
                    pastOrder.getString("products"),
                    pastOrder.getString("vouchers"),
                    pastOrder.getString("date"),
                    pastOrder.getDouble("total"))
            )
        }
        updateUserLiveData()

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

    private fun user(): List<UserRecyclerAdapterItem> {
        return pastOrders.map { pastOrder ->
            val id = pastOrder.id
            val products = pastOrder.products
            val vouchers = pastOrder.vouchers
            val date = pastOrder.date
            val total = pastOrder.total
            val allProducts = this.products
            val allVouchers = this.vouchers
            UserRecyclerAdapterItem(products, vouchers, date, total, id, allProducts, allVouchers)
        }.reversed()
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

    private fun updateUserLiveData() {
        pastOrdersLiveData.postValue(user())
    }

    fun getFormatedData(userid: String): String {

        val data = JSONObject()
        data.put("Products",cart.orderList.map { ("\"" + it.product.id.toString() + "\":" + it.quantity.toString()) }.toString().replace("[", "{").replace("]", "}"))
        data.put("Vouchers",cart.orderVoucherList.map { ("\"" + it.voucher.id + "\":" + it.voucher.type.toString()) }.toString().replace("[", "{").replace("]", "}"))
        data.put("userid", userid)
        data.put("TotalDiscounted", cart.totalCostDiscounted)
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

    override fun deletePastOrder(orderId: Int, context: Context) {
        val deleteRequest = JSONObject()
        deleteRequest.put("orderid", orderId)
        val url = Constants.serverUrl + "order/delete"

        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, deleteRequest ,
            { response ->
                println("Response is: $response")
            },
            { error ->
                println("That didn't work: $error")
            })
        queue.add(jsonObjectRequest)
        updateUserLiveData()
    }
}