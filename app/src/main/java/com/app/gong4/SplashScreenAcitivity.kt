package com.app.gong4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.gong4.model.ResponseRefreshTokenBody
import com.app.gong4.util.MainApplication

class SplashScreenAcitivity : AppCompatActivity(),AutoLoginView {

    private lateinit var mService: AutoLoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mService = AutoLoginService(this)
        mService.goServerAutoLogin()
    }

    override fun onValidateSuccess(response: ResponseRefreshTokenBody) {
        response.let { it ->
            val accessToken = it!!.data.accessToken
            Log.d("accessToken",accessToken)
            MainApplication.prefs.setData("accessToken",accessToken)
            MainApplication.prefs.setData("loginFlag","true")
        }

        goIntent(true)
    }

    override fun onValidateFail(code: Int) {
        when(code){
            404 -> Toast.makeText(applicationContext,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            400 -> {
                Thread.sleep(2000)
                MainApplication.prefs.setData("loginFlag","false")
                goIntent(false)
            }
        }
    }

    private fun goIntent(flag : Boolean){
        val intent = Intent(this@SplashScreenAcitivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}