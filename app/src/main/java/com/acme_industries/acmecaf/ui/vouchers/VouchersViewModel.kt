package com.acme_industries.acmecaf.ui.vouchers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VouchersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "My Vouchers"
    }
    val text: LiveData<String> = _text
}