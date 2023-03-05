package com.example.shoea.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import timber.log.Timber

interface ProductService {
    companion object{
        private const val BASE_URL = "https://dummyjson.com/"

        private val retrofitService by lazy {
            val httpClient = OkHttpClient()

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ProductService::class.java)
        }

        fun getInstance():ProductService{
            Timber.d("Getting Product ProductService calling getInstance() ")
            return retrofitService
        }
    }

    @GET("products")
    suspend fun getProducts(): Response<ProductList>
}