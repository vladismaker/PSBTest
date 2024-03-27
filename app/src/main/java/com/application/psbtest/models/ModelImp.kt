package com.application.psbtest.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.application.psbtest.AppContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ModelImp:Model {

    override suspend fun startRequest(url: String): String?  {
        return suspendCoroutine { continuation ->

            val client = OkHttpClient()

            val urlMy = URL(url)
            val request = Request.Builder()
                .url(urlMy)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resume("error")
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response.body?.string())
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