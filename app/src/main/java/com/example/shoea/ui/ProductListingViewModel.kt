package com.example.shoea.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoea.data.Product
import com.example.shoea.data.ProductListingRepository
import com.example.shoea.data.network.LoadingStatus
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ProductListingViewModel(application: Application) : AndroidViewModel(application) {
    private val repo:ProductListingRepository = ProductListingRepository(application)

    val products: LiveData<List<Product>> = repo.getProducts()

    private val _loadingStatus = MutableLiveData<LoadingStatus?>()
    val loadingStatus:LiveData<LoadingStatus?>
        get() = _loadingStatus


    fun fetchFromNetwork() {
        _loadingStatus.value = LoadingStatus.loading()
        viewModelScope.launch {
            _loadingStatus.value = withContext(Dispatchers.IO){
//                delay(5000)
                Timber.d("Getting Product viewModel  calling fetchFromNetwork() ")
                repo.fetchFromNetwork()
            }
        }
    }

    fun refreshData(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllData()
        }
        fetchFromNetwork()
    }

}