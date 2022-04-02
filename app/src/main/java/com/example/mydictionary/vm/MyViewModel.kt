package com.example.mydictionary.vm

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydictionary.model.MyDicList
import com.example.mydictionary.model.My_Dictionary
import com.example.mydictionary.repository.MyRespository
import com.example.myweather.retrofit.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.mobiler.mvvmg23.utils.NetworkHelper
import java.lang.Exception

class MyViewModel() : ViewModel() {


    private val myRespository = MyRespository(ApiClient.apiServis)

    var liveData = MutableLiveData<DictionaryResource>(DictionaryResource.Loading)
    val live: LiveData<DictionaryResource> = liveData

    @RequiresApi(Build.VERSION_CODES.M)
    fun getMyDictionaryData(text: String, context: Context): LiveData<DictionaryResource> {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                if (isOnline(context)) {
                    coroutineScope {
                        val myDecData = myRespository.getMyDecData(text)
                        if (myDecData.isSuccessful) {
                            liveData.postValue(myDecData.body()?.get(0)
                                ?.let { DictionaryResource.Success(it) }
                            )
                        } else {
                            liveData.postValue(DictionaryResource.Error("Error"))
                        }
                    }
                }
                //                else{
//                    liveData.postValue(DictionaryResource.Error("Network no"))
//                }
            }
        } catch (e: Exception) {
            liveData.postValue(DictionaryResource.Error("Error"))
        }

        return liveData
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getMyDictionaryDataList(context: Context): LiveData<DictionaryResource> {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                if (isOnline(context)) {
                    coroutineScope {
                        val myDecData = myRespository.getMyDecData("green")
                        val myDecData2 = myRespository.getMyDecData("house")
                        val myDecData3 = myRespository.getMyDecData("information")
                        var myDicList = ArrayList<My_Dictionary>()

                        if (myDecData.isSuccessful && myDecData2.isSuccessful && myDecData3.isSuccessful) {
                            myDicList.add(
                                (myDecData.body()?.get(0)
                                    ?: emptyList<My_Dictionary>()) as My_Dictionary
                            )
                            myDicList.add(
                                (myDecData2.body()?.get(0)
                                    ?: emptyList<My_Dictionary>()) as My_Dictionary
                            )
                            myDicList.add(
                                (myDecData3.body()?.get(0)
                                    ?: emptyList<My_Dictionary>()) as My_Dictionary
                            )

                            liveData.postValue(DictionaryResource.SuccessList(myDicList))
                        } else {
                            liveData.postValue(DictionaryResource.Error("Error"))
                        }
                    }
                } else {
                    liveData.postValue(DictionaryResource.Error("Network no"))
                }
            }
        } catch (e: Exception) {
            liveData.postValue(DictionaryResource.Error("Error"))
        }

        return liveData
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}