package com.example.mydictionary.vm

import com.example.mydictionary.model.My_Dictionary

sealed class DictionaryResource {

    object Loading : DictionaryResource()

    data class SuccessList(val myRespository: ArrayList<My_Dictionary>) : DictionaryResource()
    data class Success(val myRespository: My_Dictionary) : DictionaryResource()

    data class Error(val message: String) : DictionaryResource()


}