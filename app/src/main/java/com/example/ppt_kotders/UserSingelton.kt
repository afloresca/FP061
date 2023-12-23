package com.example.ppt_kotders

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.Locale

object UserSingelton {
    var id : Int = 0
    var uid : String = ""
    var estado = 0
    var EMAIL= "email"
    var USERNAME="username"
    var lang = Locale.getDefault().language

    private  fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun  editor(context: Context, const:String, string: String){
        getSharedPreference(
            context
        )?.edit()?.putString(const,string)?.apply()
    }

    fun getEmail(context: Context)= getSharedPreference(
        context
    )?.getString(UserSingelton.EMAIL,"")

    fun setEmail(context: Context, email: String){
        editor(
            context,
            UserSingelton.EMAIL,
            email
        )
    }

    fun setUsername(context: Context, username:String){
        editor(
            context,
            UserSingelton.USERNAME,
            username
        )
    }

    fun getUsername(context: Context) = getSharedPreference(
        context
    )?.getString(UserSingelton.USERNAME,"")

    fun setUID(newUID: String) {
        uid = newUID
    }

    fun getUID(): String {
        return uid
    }
}

