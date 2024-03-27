package com.application.psbtest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.application.psbtest.R
import com.application.psbtest.ValuteDataClass

class RecyclerViewAdapter(private val allDataList: MutableList<ValuteDataClass>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return allDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.card_two_my_app, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val tvName: TextView = cardView.findViewById(R.id.id_name)
        val layout: CardView = cardView.findViewById(R.id.card_view)
        val tvPrice: TextView = cardView.findViewById(R.id.id_price)


        if(checkEvenOrOdd(position)){
            layout.setBackgroundResource(R.color.grey)
        }else{
            layout.setBackgroundResource(R.color.white)
        }

        tvName.text = allDataList[position].name
        val doublePrice = allDataList[position].value.toDouble()
        val stringPrice = "%.3f".format(doublePrice)
        tvPrice.text = stringPrice
    }

    private fun checkEvenOrOdd(number: Int): Boolean {
        return number % 2 == 0
    }
}