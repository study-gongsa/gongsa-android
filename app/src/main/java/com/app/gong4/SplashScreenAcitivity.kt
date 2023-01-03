package com.app.gong4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.app.gong4.model.req.RequestRefreshTokenBody
import com.app.gong4.model.res.ResponseRefreshTokenBody
import com.app.gong4.utils.NetworkResult
import com.app.gong4.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenAcitivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goServerAutoLogin()
    }

    fun goServerAutoLogin(){
        userViewModel.loginRefreshRes.observe(this){
            when(it){
                is NetworkResult.Success -> {
                    onValidateSuccess(it.data!!)
                }
                is NetworkResult.Error -> {
                    onValidateFail(400)
                }
                else -> {
                    onValidateFail(404)
                }
            }
        }

        val refreshToken: String = MainApplication.tokenManager.getRefreshToken()!!
        if(refreshToken == ""){ // 앱 설치 후 최초 로그인
            goIntent()
        }else{
            userViewModel.refreshToken(RequestRefreshTokenBody(refreshToken))
        }
    }

    private fun onValidateSuccess(response: ResponseRefreshTokenBody) {
        response.let {
            val accessToken = it!!.data.accessToken

            MainApplication.tokenManager.saveAccessToken(accessToken)
            MainApplication.tokenManager.saveLoginFlag("true")
        }

        goIntent()
    }

    private fun onValidateFail(code: Int) {
        when(code){
            404 -> Toast.makeText(applicationContext,"서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            400 -> {
                Thread.sleep(2000)
                MainApplication.tokenManager.saveLoginFlag("false")
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