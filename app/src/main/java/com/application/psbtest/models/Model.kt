package com.application.psbtest.models

interface Model {
    suspend fun startRequestRetrofit(url:String):String?
    suspend fun checkInternet():Boolean
}