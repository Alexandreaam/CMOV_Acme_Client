package com.acme_industries.acmecaf.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.R
import org.json.JSONObject
import java.lang.Integer.parseInt

class UserRecyclerAdapter() : RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>() {


    var pastOrders: List<UserRecyclerAdapterItem> = emptyList()

    interface UserClickListener {
        fun deletePastOrder(orderId: Int)
    }

    var userClickListener: UserClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderNumber: TextView = itemView.findViewById(R.id.order_number)
        var orderTotal: TextView = itemView.findViewById(R.id.order_total)
        var orderSummary: TextView = itemView.findViewById(R.id.order_summary)
        var orderDate: TextView = itemView.findViewById(R.id.order_date)

        fun bind(pastOrder: UserRecyclerAdapterItem, userClickListener: UserClickListener?) {

            var productsJS = JSONObject(pastOrder.products)
            var vouchersJS = JSONObject(pastOrder.vouchers)

            var tempOrderSummary = ""

            val keys: Iterator<String> = productsJS.keys()

            while (keys.hasNext()) {
                val key = keys.next()
                tempOrderSummary += pastOrder.allProducts[parseInt(key)].title + " x " + productsJS.get(key).toString() + "\n"
            }

            var tempVoucherSummary = ""

            val keys2: Iterator<String> = vouchersJS.keys()

            while (keys2.hasNext()) {
                val key = keys2.next()
                if(vouchersJS.get(key).toString() == "true")
                    tempVoucherSummary += "Free Coffee Voucher\n"
                else if(vouchersJS.get(key).toString() == "false")
                    tempVoucherSummary += "5% Discount Voucher\n"
            }

            orderNumber.text = "Order #" + pastOrder.id.toString()
            orderTotal.text = "%.2f€".format(pastOrder.total)
            orderSummary.text = tempOrderSummary + "\n" + tempVoucherSummary
            orderDate.text = pastOrder.date.split("T")[0]


            val orderRemove: ImageView = itemView.findViewById(R.id.rem_butt)

            orderRemove.setOnClickListener {
                userClickListener?.deletePastOrder(pastOrder.id)
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.order_card, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(pastOrders[i], userClickListener)
    }

    override fun getItemCount(): Int {
        return pastOrders.size
    }

    fun refreshData(pastOrders: List<UserRecyclerAdapterItem>) {
        this.pastOrders = pastOrders
        notifyDataSetChanged()
    }
}