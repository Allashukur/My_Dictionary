package com.example.mydictionary.repository

import com.example.mydictionary.model.My_Dictionary
import com.example.myweather.retrofit.ApiServis
import retrofit2.Response

open class MyRespository(private val apiServis: ApiServis) {

    suspend fun getMyDecData(textQuery: String): Response<List<My_Dictionary>> = apiServis.getMyDic(textQuery)
}