package com.acme_industries.acmecaf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acme_industries.acmecaf.core.Product

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView = itemView.findViewById(R.id.prod_img)
        var itemTitle: TextView = itemView.findViewById(R.id.prod_name)
        var itemDetail: TextView = itemView.findViewById(R.id.prod_price)
        var itemQuantity: TextView = itemView.findViewById(R.id.prod_quant)
        private var addButton: ImageButton = itemView.findViewById(R.id.add_butt)
        private var removeButton: ImageButton = itemView.findViewById(R.id.rem_butt)

        fun bind (product: Product) {
            itemTitle.text = product.title
            itemDetail.text = product.details
            itemQuantity.text = product.quantity.toString()
            itemImage.setImageResource(product.image)

            addButton.setOnClickListener {
                product.quantity += 1
                itemQuantity.text = product.quantity.toString()
            }
            removeButton.setOnClickListener {
                if(product.quantity > 0) {
                    product.quantity -= 1
                    itemQuantity.text = product.quantity.toString()
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

    private val products = arrayOf(Product("Coffee", "0.50€", R.drawable.main_icon),
            Product("Panini", "1.00€", R.drawable.main_icon),
            Product("Mixed Toast", "1.30€", R.drawable.main_icon),
            Product("Croissant", "1.40€", R.drawable.main_icon),
            Product("Half-of-Milk", "1.00€", R.drawable.main_icon),
            Product("Finou", "1.00€", R.drawable.main_icon))

}