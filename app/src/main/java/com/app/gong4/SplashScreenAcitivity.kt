package com.app.gong4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.gong4.model.res.ResponseRefreshTokenBody
import com.app.gong4.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenAcitivity : AppCompatActivity(),AutoLoginView {

    private lateinit var mService: AutoLoginService

    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(applicationContext)
        mService = AutoLoginService(this,tokenManager)
        mService.goServerAutoLogin()
    }

    override fun onValidateSuccess(response: ResponseRefreshTokenBody) {
        response.let {
            val accessToken = it!!.data.accessToken

            tokenManager.saveAccessToken(accessToken)
            tokenManager.saveLoginFlag("true")
        }

        goIntent()
    }

    override fun onValidateFail(code: Int) {
        when(code){
            404 -> Toast.makeText(applicationContext,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            400 -> {
                Thread.sleep(2000)
                tokenManager.saveLoginFlag("false")
                goIntent()
            }
        }
    }

    private fun goIntent(){
        val intent = Intent(this@SplashScreenAcitivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}