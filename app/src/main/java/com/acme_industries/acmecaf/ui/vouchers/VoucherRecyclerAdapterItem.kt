package com.acme_industries.acmecaf.ui.vouchers

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter

class VoucherRecyclerAdapterItem (val id: Int, val title: String, val details: String, val image: String, val quantity: Int, val total: Int, val use: Boolean) {
    val addColorFilter = if (quantity == total) { PorterDuffColorFilter(Color.argb(150,200,200,200), PorterDuff.Mode.SRC_ATOP)  } else null
    val remColorFilter = if (quantity == 0) { PorterDuffColorFilter(Color.argb(150,200,200,200), PorterDuff.Mode.SRC_ATOP)  } else null
}