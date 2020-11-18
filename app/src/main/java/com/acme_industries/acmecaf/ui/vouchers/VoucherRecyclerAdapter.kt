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
    }

    var voucherClickListener: VoucherClickListener? = null

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var vouchImage: ImageView = itemView.findViewById(R.id.vouch_img)
        var vouchTitle: TextView = itemView.findViewById(R.id.vouch_name)
        var vouchDetail: TextView = itemView.findViewById(R.id.vouch_desc)
        var vouchQuantity: TextView = itemView.findViewById(R.id.vouch_quant)
        private var vouchCheck: CheckBox = itemView.findViewById(R.id.vouch_check)

        fun bind (voucher: VoucherRecyclerAdapterItem, voucherClickListener: VoucherClickListener?) {

            vouchTitle.text = voucher.title
            vouchDetail.text = voucher.details
            vouchQuantity.text = voucher.quantity.toString() + " available vouchers"
            vouchCheck.setChecked(voucher.use)

            Glide.with(itemView)
                .load(Constants.serverUrl + voucher.image)
                .into(vouchImage)

            vouchCheck.setOnClickListener{
                voucherClickListener?.checkVouch(voucher.title)
            }

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
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