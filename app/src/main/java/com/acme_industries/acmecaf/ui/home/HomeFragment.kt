package com.acme_industries.acmecaf.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.core.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null

    private val cartModel: MainViewModel by activityViewModels()
    var adapterOrders = OrdersRecyclerAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager

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
        return root
    }


}