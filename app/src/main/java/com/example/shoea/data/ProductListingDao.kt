package com.example.shoea.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductListingDao {
    @Query("SELECT * FROM product")
    fun getProducts():LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(productList:List<Product>)

    @Query("DELETE FROM product")
    suspend fun deleteAllData()
}