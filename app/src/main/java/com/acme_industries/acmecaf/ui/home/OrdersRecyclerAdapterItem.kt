package com.acme_industries.acmecaf.ui.home

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter

data class OrdersRecyclerAdapterItem (val title: String, val details: String, val price: Double, val image: String, val quantity: Int){
    val colorFilter = if (quantity == 0) { PorterDuffColorFilter(Color.argb(150,200,200,200), PorterDuff.Mode.SRC_ATOP)  } else null
}
