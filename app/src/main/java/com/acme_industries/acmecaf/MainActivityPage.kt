package com.acme_industries.acmecaf

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.acme_industries.acmecaf.core.Constants
import com.acme_industries.acmecaf.core.MainViewModel
import com.acme_industries.acmecaf.core.QRBuilder
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class MainActivityPage : AppCompatActivity() {

    private val cartModel: MainViewModel by viewModels()
    private var userid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_main_page)

        reload()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_menu, R.id.navigation_vouchers, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun reload() {
        userid = intent.getStringExtra("userid").toString()

        cartModel.loadData(userid, this.application.applicationContext)

        findViewById<Button>(R.id.checkout_button).setOnClickListener { checkout() }
        findViewById<FloatingActionButton>(R.id.refresh_button).setOnClickListener {
            cartModel.reset()
            reload()
        }

        cartModel.itemsLiveData.observe(this) {
            if(cartModel.cart.orderList.isEmpty())
                findViewById<Button>(R.id.checkout_button).visibility = View.INVISIBLE
            else
                findViewById<Button>(R.id.checkout_button).visibility = View.VISIBLE
        }
        cartModel.vouchersLiveData.observe(this) { }
        cartModel.pastOrdersLiveData.observe(this) {  }

    }

    private fun checkout() {
        val modalSheetView = layoutInflater.inflate(R.layout.bottom_layout_sheet,null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(modalSheetView)

        val productlist = modalSheetView.findViewById<TextView>(R.id.checkout_resume)
        val voucherlist = modalSheetView.findViewById<TextView>(R.id.checkout_voucher_resume)
        val total = modalSheetView.findViewById<TextView>(R.id.checkout_total)
        var temp = ""

        cartModel.cart.orderList.map { temp += (it.product.title + " x " + it.quantity + "\n")}.toString()
        productlist.text = temp

        temp = ""
        cartModel.cart.orderVoucherList.map { temp += (it.voucher.title + "\n")}.toString()
        if(cartModel.has5disc)
            temp += "\nOnly one 5% discount is used!"
        voucherlist.text = temp

        total.text = "Total: ${"%.2f€".format(cartModel.cart.totalCostDiscounted)}"

        val qr = QRBuilder(this)
        val bitmap = qr.encodeAsBitmap(cartModel.getFormatedData(userid))
        val image = modalSheetView.findViewById<ImageView>(R.id.qr_image)
        image.setImageBitmap(bitmap)

        dialog.show()

/*
        val url = Constants.serverUrl + "order"
        val queue = Volley.newRequestQueue(this)

        val orderTest = JSONObject()
        orderTest.put("Products",cartModel.cart.orderList.map { ("\"" + it.product.id.toString() + "\":" + it.quantity.toString()) }.toString().replace("[", "{").replace("]", "}"))
        orderTest.put("Vouchers",cartModel.cart.orderVoucherList.map { ("\"" + it.voucher.id + "\":" + it.voucher.type.toString()) }.toString().replace("[", "{").replace("]", "}"))
        orderTest.put("userid", userid)
        orderTest.put("TotalDiscounted", cartModel.cart.totalCostDiscounted)
        orderTest.put("Total", cartModel.cart.totalCost)


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, orderTest ,
            { response ->
                println("Response is: $response")
            },
            { error ->
                println("That didn't work: ${error.message}")
            })
        queue.add(jsonObjectRequest)
*/
        findViewById<Button>(R.id.checkout_button).visibility = View.INVISIBLE
    }
}