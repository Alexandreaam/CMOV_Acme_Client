package com.acme_industries.acmecaf.ui.vouchers

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

class VoucherRecyclerAdapter() : RecyclerView.Adapter<VoucherRecyclerAdapter.ViewHolder>() {


    var vouchers: List<VoucherRecyclerAdapterItem> = emptyList()

    interface VoucherClickListener {
        fun checkVouch(vouchName: String)
        fun incrementVoucherQuantity(vouchName: String)
        fun decreaseVoucherQuantity(vouchName: String)
    }

    var voucherClickListener: VoucherClickListener? = null

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var vouchImage: ImageView = itemView.findViewById(R.id.vouch_img)
        var vouchTitle: TextView = itemView.findViewById(R.id.vouch_name)
        var vouchDetail: TextView = itemView.findViewById(R.id.vouch_desc)
        var vouchQuantity: TextView = itemView.findViewById(R.id.vouch_quant)

        fun bind (voucher: VoucherRecyclerAdapterItem, voucherClickListener: VoucherClickListener?) {

            vouchTitle.text = voucher.title
            vouchDetail.text = voucher.details
            vouchQuantity.text = "Available vouchers: " + voucher.total.toString()

            Glide.with(itemView)
                .load(Constants.serverUrl + voucher.image)
                .into(vouchImage)

            //TODO(Add type check)
            if(voucher.id == 1) {
                val addButton: ImageView = itemView.findViewById(R.id.vouch_add_butt)
                val removeButton: ImageView = itemView.findViewById(R.id.vouch_rem_butt)
                val vouchUseQuant: TextView = itemView.findViewById(R.id.vouch_use_quantity)

                addButton.visibility = View.VISIBLE
                removeButton.visibility = View.VISIBLE
                vouchUseQuant.visibility = View.VISIBLE

                addButton.colorFilter = voucher.addColorFilter
                removeButton.colorFilter = voucher.remColorFilter

                vouchUseQuant.text = voucher.quantity.toString()

                addButton.setOnClickListener {
                    voucherClickListener?.incrementVoucherQuantity(voucher.title)
                }
                removeButton.setOnClickListener {
                    voucherClickListener?.decreaseVoucherQuantity(voucher.title)
                }
            }
            if(voucher.id == 2) {
                val vouchCheck: CheckBox = itemView.findViewById(R.id.vouch_check)

                vouchCheck.visibility = View.VISIBLE

                vouchCheck.setChecked(voucher.use)
                vouchCheck.setOnClickListener {
                    voucherClickListener?.checkVouch(voucher.title)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.voucher_card, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(vouchers[i], voucherClickListener)
    }

    override fun getItemCount(): Int {
        return vouchers.size
    }

    fun refreshData(vouchers: List<VoucherRecyclerAdapterItem>) {
        this.vouchers = vouchers
        notifyDataSetChanged()
    }
}