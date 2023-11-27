package com.example.ppt_kotders

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

object UserSingelton {
    private var defaultLan = Locale.getDefault().language
    var id : Int = 0
    var estado = 0
    var lang = defaultLan // "en o es "

fun cambiarlan(cadena : String,context: Context){
    val locale = Locale(cadena)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    Resources.getSystem().updateConfiguration(config, Resources.getSystem().getDisplayMetrics()
    )
    lang = cadena

}
}