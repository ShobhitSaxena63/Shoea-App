package com.example.shoea.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.shoea.data.network.ErrorCode
import com.example.shoea.data.network.LoadingStatus
import com.example.shoea.data.network.ProductService
import timber.log.Timber
import java.net.UnknownHostException

class ProductListingRepository(context:Application){
    private val productListingDao:ProductListingDao = ProductDatabase.getDatabase(context).productListingDao()
    private val productService by lazy{ ProductService.getInstance() }

    fun getProducts():LiveData<List<Product>>{
        return productListingDao.getProducts()
    }


    //    region Error Handling
    suspend fun fetchFromNetwork() =
        try{
            val result = productService.getProducts()
            Timber.d("Getting Product repository $result ")
            if(result.isSuccessful) {
                val productList = result.body()
                Timber.d("Getting Product $productList")
                productList?.let {productListingDao.insertProduct(it.products) }
                LoadingStatus.success()
            } else{
                LoadingStatus.error(ErrorCode.NO_DATA)
            }
        }catch (ex: UnknownHostException){
            LoadingStatus.error(ErrorCode.NETWORK_ERROR)
        }catch (ex:Exception){
            LoadingStatus.error(ErrorCode.UNKNOWN_ERROR,ex.message)
        }
    //endregion

    suspend fun deleteAllData(){
        productListingDao.deleteAllData()
    }
}