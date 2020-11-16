package com.acme_industries.acmecaf.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.R
import com.acme_industries.acmecaf.core.*
import com.bumptech.glide.Glide

class OrdersRecyclerAdapter() : RecyclerView.Adapter<OrdersRecyclerAdapter.ViewHolder>() {

    var products: List<OrdersRecyclerAdapterItem> = emptyList()

    interface ItemClickListener {
        fun incrementQuantity(productName: String)
        fun decreaseQuantity(productName: String)
    }

    var itemClickListener: ItemClickListener? = null

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView = itemView.findViewById(R.id.prod_img)
        var itemTitle: TextView = itemView.findViewById(R.id.prod_name)
        var itemDetail: TextView = itemView.findViewById(R.id.prod_desc)
        var itemPrice: TextView = itemView.findViewById(R.id.prod_price)
        var itemQuantity: TextView = itemView.findViewById(R.id.prod_quant)
        private var addButton: ImageView = itemView.findViewById(R.id.add_butt)
        private var removeButton: ImageView = itemView.findViewById(R.id.rem_butt)

        fun bind (order: OrdersRecyclerAdapterItem, itemClickListener: ItemClickListener?) {

            itemTitle.text = order.title
            itemDetail.text = order.details
            itemPrice.text = "%.2f€".format(order.price)
            itemQuantity.text = order.quantity.toString()
            removeButton.colorFilter = order.colorFilter

            Glide.with(itemView)
                    .load(Constants.serverUrl + order.image)
                    .into(itemImage)

            addButton.setOnClickListener {
                itemClickListener?.incrementQuantity(order.title)
            }
            removeButton.setOnClickListener {
                itemClickListener?.decreaseQuantity(order.title)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_card, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(products[i], itemClickListener)
    }
    override fun getItemCount(): Int {
        return products.size
    }
    fun refreshData(products: List<OrdersRecyclerAdapterItem>) {
        this.products = products
        notifyDataSetChanged()
    }
}