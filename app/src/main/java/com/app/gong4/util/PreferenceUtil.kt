package com.app.gong4.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs : SharedPreferences = context.getSharedPreferences("pref_app",Context.MODE_PRIVATE)

    fun getData(key : String, value : String):String{
        return prefs.getString(key,value).toString()
    }

    fun setData(key: String, str: String){
        prefs.edit().putString(key,str).apply()
    }
}