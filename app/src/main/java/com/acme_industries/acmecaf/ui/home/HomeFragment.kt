package com.acme_industries.acmecaf.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.core.MainViewModel
import com.acme_industries.acmecaf.core.QRBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null

    val cartModel: MainViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager

        val adapterOrders = OrdersRecyclerAdapter()

        cartModel.itemsLiveData.observe(viewLifecycleOwner) {
            adapterOrders.refreshData(it)
        }

        adapterOrders.itemClickListener = cartModel
        recycler_view.adapter = adapterOrders
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val checkoutButton: ExtendedFloatingActionButton = root.findViewById(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            checkout()
        }
        return root
    }

    private fun checkout() {

    }
/*
    fun testQR(view: View) {
        val qr = QRBuilder(this)
        val bitmap = qr.encodeAsBitmap("This is a QR code")
        val image = findViewById<ImageView>(R.id.imageView2)
        image.setImageBitmap(bitmap)
    }
*/
}