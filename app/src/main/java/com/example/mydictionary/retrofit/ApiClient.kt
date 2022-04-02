package com.example.myweather.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {

    const val BASE_URL =
        "https://api.dictionaryapi.dev/"


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiServis = getRetrofit().create(ApiServis::class.java)


}