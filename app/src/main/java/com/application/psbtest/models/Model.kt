package com.application.psbtest.models

interface Model {
    suspend fun startRequest(url:String):String?
    suspend fun checkInternet():Boolean
}