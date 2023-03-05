package com.example.shoea.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@TypeConverters(ListTypeConverters::class)
@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productListingDao():ProductListingDao
    abstract fun productDetailDao():ProductDetailDao

    companion object{
        @Volatile
        private var instance:ProductDatabase? = null

        fun getDatabase(context: Context) = instance?: synchronized(this){
            Room.databaseBuilder(context.applicationContext,
                ProductDatabase::class.java,
                "product_database").build().also { instance = it }
        }
    }
}