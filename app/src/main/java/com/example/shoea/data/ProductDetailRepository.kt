package com.example.shoea.data

import android.app.Application
import androidx.lifecycle.LiveData

class ProductDetailRepository(context: Application) {
    private val productDetailDao:ProductDetailDao = ProductDatabase.getDatabase(context).productDetailDao()

    fun getProducts(id:Long):LiveData<Product>{
        return productDetailDao.getProduct(id)
    }
}