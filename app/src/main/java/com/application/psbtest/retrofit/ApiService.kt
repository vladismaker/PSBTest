package com.application.psbtest.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("daily_json.js")
    fun getRequest(): Call<ResponseBody>
}