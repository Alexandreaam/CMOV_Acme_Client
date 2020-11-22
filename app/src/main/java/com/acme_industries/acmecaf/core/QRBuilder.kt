package com.acme_industries.acmecaf.core

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.acme_industries.acmecaf.R

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix


class QRBuilder (var context: Context){

    fun encodeAsBitmap(str: String?): Bitmap? {
        val result = MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 800, 800, null)
        val w: Int = result.width
        val h: Int = result.height
        val pixels = IntArray(w * h)
        for (line in 0 until h) {
            val offset = line * w
            for (col in 0 until w) {
                pixels[offset + col] = if (result.get(
                        col,
                        line
                    )
                ) ContextCompat.getColor(context, R.color.colorPrimary) else ContextCompat.getColor(context, R.color.colorOnSecondary)
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}