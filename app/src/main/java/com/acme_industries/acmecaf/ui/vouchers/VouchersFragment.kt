package com.acme_industries.acmecaf.ui.vouchers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.acme_industries.acmecaf.R

class VouchersFragment : Fragment() {

    private lateinit var vouchersViewModel: VouchersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vouchersViewModel =
            ViewModelProvider(this).get(VouchersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_vouchers, container, false)
        val textView: TextView = root.findViewById(R.id.voucher_title)
        vouchersViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}