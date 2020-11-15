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

class MainActivityPage : AppCompatActivity() {

    val cartModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_main_page)

        val url = Constants.serverUrl + "menu"

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null ,
            { response ->
                println("Response is: $response")
                cartModel.productMessageParse(response)
            },
            { error ->
                println("That didn't work: $error")
            })
        queue.add(jsonObjectRequest)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        findViewById<Button>(R.id.checkout_button).setOnClickListener { checkout() }

        cartModel.itemsLiveData.observe(this) {
            if(cartModel.cart.orderList.isEmpty())
                findViewById<Button>(R.id.checkout_button).visibility = View.INVISIBLE
            else
                findViewById<Button>(R.id.checkout_button).visibility = View.VISIBLE
        }

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_menu, R.id.navigation_vouchers, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun checkout() {
        val modalSheetView = layoutInflater.inflate(R.layout.bottom_layout_sheet,null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(modalSheetView)

        val list = modalSheetView.findViewById<TextView>(R.id.checkout_resume)
        val total = modalSheetView.findViewById<TextView>(R.id.checkout_total)
        var temp = ""

        cartModel.cart.orderList.map { temp += (it.product.title + " x " + it.quantity + "\n")}.toString()
        list.text = temp
        total.text = "Total:${"%.2f€".format(cartModel.cart.totalCost)}"

        //TODO Add user key
        val qr = QRBuilder(this)
        val bitmap = qr.encodeAsBitmap(cartModel.getFormatedData())
        val image = modalSheetView.findViewById<ImageView>(R.id.qr_image)
        image.setImageBitmap(bitmap)


        dialog.show()
    }
}