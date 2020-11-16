package com.acme_industries.acmecaf.ui.vouchers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.core.MainViewModel
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class VouchersFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null

    val cartModel: MainViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager

        val adapterVouchers = VoucherRecyclerAdapter()

        cartModel.vouchersLiveData.observe(viewLifecycleOwner) {
            adapterVouchers.refreshData(it)
        }

        adapterVouchers.voucherClickListener = cartModel
        recycler_view.adapter = adapterVouchers
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vouchers, container, false)
    }
}