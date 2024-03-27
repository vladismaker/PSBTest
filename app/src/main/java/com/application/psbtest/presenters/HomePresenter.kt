package com.application.psbtest.presenters

import com.application.psbtest.ValuteDataClass
import com.application.psbtest.models.Model
import com.application.psbtest.straings_interactor.StringsInteractor
import com.application.psbtest.views.HomeView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class HomePresenter(private val view:HomeView, private val stringsInteractor: StringsInteractor, private val model: Model) {
    private var dataNewFromMapInternet: MutableList<ValuteDataClass> = mutableListOf()
    private var dataBoolean:Boolean = false
    private var timerJob: Timer? = null
    private var coroutine: Job? = null
    private var newCoroutine: Job? = null

    fun startPresenter(){
        timerJob?.cancel()
        coroutine?.cancel()
        newCoroutine?.cancel()

        coroutine = CoroutineScope(Dispatchers.IO).launch {
            timerJob = fixedRateTimer(period = 30_000) {

                newCoroutine?.cancel()
                newCoroutine = CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main){
                        view.showUpdate()
                    }
                    try {
                        val internet = async {model.checkInternet()}.await()
                        if(!internet){
                            withContext(Dispatchers.Main){
                                view.showToast(stringsInteractor.textInternet)
                                view.showButtonForUpdate()
                            }
                            coroutine?.cancel()
                            newCoroutine?.cancel()
                            timerJob?.cancel()

                        }else{
                            val fullUrl = "https://www.cbr-xml-daily.ru/daily_json.js"
                            val data  = async {model.startRequestRetrofit(fullUrl)}.await()
                            if (data!=null && data!="error"){
                                dataFromJson(data.toString())
                                withContext(Dispatchers.Main){
                                    view.showDate(getDateNow())
                                    view.setRecyclerView(dataNewFromMapInternet)
                                }
                            }else{
                                view.showToast("Ошибка запроса")
                            }

                        }
                    }catch (e: Throwable) {
                        withContext(Dispatchers.Main){
                            view.setRecyclerView(dataNewFromMapInternet)
                        }
                    }
                }
            }
        }
    }

    private fun getDateNow():String{
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return currentDateTime.format(formatter)
    }

    private fun dataFromJson(response: String){
        dataNewFromMapInternet.clear()

        val jObj = JSONObject(response)
        val valuteJsonObject = jObj.getJSONObject("Valute")

        valuteJsonObject.keys().forEach { key ->
            val valute = valuteJsonObject.getJSONObject(key)
            val id = valute.getString("ID").hashCode()
            val name = valute.getString("Name")
            val charCode = valute.getString("CharCode")
            val nominal = valute.getInt("Nominal").toString()
            val value = valute.getDouble("Value").toString()

            val valuteData = ValuteDataClass(id, name, charCode, nominal, value)
            dataNewFromMapInternet.add(valuteData)
        }

        dataBoolean=true
    }

    fun onDestroy(){
        coroutine?.cancel()
        timerJob?.cancel()
        newCoroutine?.cancel()
    }
}