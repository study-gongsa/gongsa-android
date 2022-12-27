package com.app.gong4.utils

import android.content.Context
import android.content.SharedPreferences
import com.app.gong4.utils.Constants.LOGIN_FLAG
import com.app.gong4.utils.Constants.PREFS_APP_FILE
import com.app.gong4.utils.Constants.USER_ACCESS_TOKEN
import com.app.gong4.utils.Constants.USER_REFRESH_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs : SharedPreferences = context.getSharedPreferences(PREFS_APP_FILE,Context.MODE_PRIVATE)

    fun saveAccessToken(token:String){
        prefs.edit().putString(USER_ACCESS_TOKEN,token).apply()
    }

    fun getAccessToken() : String?{
        return prefs.getString(USER_ACCESS_TOKEN,null)
    }

    fun saveRefreshToken(token:String){
        prefs.edit().putString(USER_REFRESH_TOKEN,token).apply()
    }

    fun getRefreshToken():String?{
        return prefs.getString(USER_REFRESH_TOKEN,null)
    }

    fun saveLoginFlag(flag:String){
        prefs.edit().putString(LOGIN_FLAG,flag).apply()
    }

    fun getLoginFlag():String?{
        return prefs.getString(LOGIN_FLAG,null)
    }
}