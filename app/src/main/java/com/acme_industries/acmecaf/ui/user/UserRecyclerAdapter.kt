package com.acme_industries.acmecaf.ui.user

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.core.Constants
import com.bumptech.glide.Glide

class UserRecyclerAdapter() : RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>() {


    var pastOrders: List<UserRecyclerAdapterItem> = emptyList()

    interface UserClickListener {
        fun deletePastOrder(orderId: Int)
    }

    var userClickListener: UserClickListener? = null

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var orderNumber: TextView = itemView.findViewById(R.id.order_number)
        var orderTotal: TextView = itemView.findViewById(R.id.order_total)
        var orderSummary: TextView = itemView.findViewById(R.id.order_summary)
        var orderDate: TextView = itemView.findViewById(R.id.order_date)

        fun bind (pastOrder: UserRecyclerAdapterItem, userClickListener: UserClickListener?) {

            orderNumber.text = "Order #" + pastOrder.id.toString()
            orderTotal.text = "%.2f€".format(pastOrder.total)
            orderSummary.text = pastOrder.products.toString() + pastOrder.vouchers.toString()
            orderDate.text = pastOrder.date.toString()


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