package com.example.tasin_23_rest01.network

import com.example.tasin_23_rest01.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    fun getProducts(): Call<List<Product>>
}