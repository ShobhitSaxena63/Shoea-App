package com.example.shoea.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProductDetailDao {
    @Query("SELECT * FROM product WHERE `id`=:id")
    fun getProduct(id:Long):LiveData<Product>
}