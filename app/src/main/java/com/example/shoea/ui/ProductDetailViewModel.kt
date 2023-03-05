package com.example.shoea.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoea.data.Product
import com.example.shoea.data.ProductDetailRepository
import timber.log.Timber

class ProductDetailViewModel(id:Long,quantity:Int,application: Application): AndroidViewModel(application) {
    private val repo:ProductDetailRepository = ProductDetailRepository(application)


    val products:LiveData<Product> = repo.getProducts(id)

    private var _qty = MutableLiveData<Int>(quantity)
    private val qty: LiveData<Int>
        get()  = _qty

    fun setQty(value:Int) {
        _qty.value = value
    }

    fun getQty():Int {
        return qty.value!!
    }

//    To handle viewpager config changes
    private var _currentItem = MutableLiveData<Int>(0)
    private val currentItem: LiveData<Int>
        get()  = _currentItem

    fun setCurrentItem(value:Int) {
        _currentItem.value = value
    }

    fun getCurrentItem():Int {
        return currentItem.value!!
    }

}