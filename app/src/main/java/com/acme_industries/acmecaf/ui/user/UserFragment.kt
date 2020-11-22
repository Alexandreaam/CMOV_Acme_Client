package com.acme_industries.acmecaf.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.core.MainViewModel
import com.acme_industries.acmecaf.ui.home.OrdersRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class UserFragment : Fragment() {

    private val cartModel: MainViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val root = inflater.inflate(R.layout.fragment_user, container, false)

        root.findViewById<TextView>(R.id.total_coff_card_value).text = cartModel.userStats?.coffeecount.toString()

        root.findViewById<TextView>(R.id.total_spent_value).text = "${"%.2f€".format(cartModel.userStats?.totalspendings)}"

        root.findViewById<TextView>(R.id.coff_until_disc_value).text = cartModel.userStats?.tempcoffeecount.toString() + "/3"

        root.findViewById<TextView>(R.id.money_until_disc_value).text = "${"%.2f€".format(cartModel.userStats?.tempspendings)}/100€"

        return root
    }
}