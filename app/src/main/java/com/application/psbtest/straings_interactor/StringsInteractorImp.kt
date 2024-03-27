package com.application.psbtest.straings_interactor

import android.content.Context
import com.application.psbtest.R

class StringsInteractorImp(private val context: Context):StringsInteractor {
    override val textInternet: String
        get() = context.getString(R.string.check_internet)

}