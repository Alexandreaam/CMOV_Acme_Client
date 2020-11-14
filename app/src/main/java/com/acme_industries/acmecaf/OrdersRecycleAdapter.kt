package com.acme_industries.acmecaf

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.core.Constants
import com.acme_industries.acmecaf.core.Product
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class RecyclerAdapter(val products: ArrayList<Product>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView = itemView.findViewById(R.id.prod_img)
        var itemTitle: TextView = itemView.findViewById(R.id.prod_name)
        var itemDetail: TextView = itemView.findViewById(R.id.prod_desc)
        var itemPrice: TextView = itemView.findViewById(R.id.prod_price)
        var itemQuantity: TextView = itemView.findViewById(R.id.prod_quant)
        private var addButton: ImageView = itemView.findViewById(R.id.add_butt)
        private var removeButton: ImageView = itemView.findViewById(R.id.rem_butt)

        fun bind (product: Product) {

            itemTitle.text = product.title
            itemDetail.text = product.details
            itemPrice.text = product.price.toString() + "€"
            itemQuantity.text = product.quantity.toString()
            removeButton.setColorFilter(Color.argb(150,200,200,200));
            removeButton.tag = "grayed";
            Glide.with(itemView)
                    .load(Constants.serverUrl + product.image)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                    .into(itemImage)

            addButton.setOnClickListener {
                product.quantity += 1
                itemQuantity.text = product.quantity.toString()
                removeButton.colorFilter = null;

            }
            removeButton.setOnClickListener {
                if(product.quantity > 0) {
                    product.quantity -= 1
                    itemQuantity.text = product.quantity.toString()
                    if(product.quantity == 0) {
                        removeButton.setColorFilter(Color.argb(150,200,200,200));
                        removeButton.tag = "grayed";
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.food_card, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(products[i])
    }
    override fun getItemCount(): Int {
        return products.size
    }
}