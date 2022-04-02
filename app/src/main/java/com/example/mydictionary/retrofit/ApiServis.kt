package com.example.myweather.retrofit

import com.example.mydictionary.model.My_Dictionary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServis {

    @GET("/api/v2/entries/en/{text}")
    suspend fun getMyDic(@Path("text") text: String): Response<List<My_Dictionary>>


}