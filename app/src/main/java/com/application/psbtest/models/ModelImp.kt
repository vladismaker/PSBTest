package com.application.psbtest.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.application.psbtest.AppContext
import com.application.psbtest.retrofit.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ModelImp:Model {
    override suspend fun startRequestRetrofit(url: String): String? {
        return suspendCoroutine { continuation ->
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.cbr-xml-daily.ru/")
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val call = apiService.getRequest()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()?.string())
                    } else {
                        continuation.resume("error")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resume("error")
                }
            })
        }
    }

    override suspend fun checkInternet(): Boolean {
        return suspendCoroutine { continuation ->
            val connectivityManager = AppContext.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNet = connectivityManager.activeNetwork
            if (activeNet != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNet)
                val resp = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                continuation.resume(resp)
            }else{
                continuation.resume(false)
            }
        }
    }
}