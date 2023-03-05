package com.example.shoea.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductDetailViewModelFactory(private val id: Long, private val quantity: Int, private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailViewModel(id,quantity,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}