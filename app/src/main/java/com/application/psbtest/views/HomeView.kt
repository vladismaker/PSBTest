package com.application.psbtest.views

import com.application.psbtest.ValuteDataClass

interface HomeView {
    fun showToast(text:String)
    fun showUpdate()
    fun showButtonForUpdate()
    fun showDate(date:String)
    fun setRecyclerView(arrayAllData:MutableList<ValuteDataClass>)
}