package com.example.shoea.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product")
data class Product(
    @PrimaryKey val id: Long,
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val images: List<String>,
    val price: Int,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String
)