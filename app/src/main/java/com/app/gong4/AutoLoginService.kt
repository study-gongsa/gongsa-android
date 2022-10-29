package com.app.gong4

import android.util.Log
import com.app.gong4.DTO.RequestRefreshTokenBody
import com.app.gong4.DTO.ResponseRefreshTokenBody
import com.app.gong4.api.RequestServer
import com.app.gong4.util.MainApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AutoLoginService {
    private lateinit var mAutoLoinView: AutoLoginView


    val USER_TOKEN = MainApplication.prefs.getData("accessToken","")

    constructor(mAutoLoinView: AutoLoginView) {
        this.mAutoLoinView = mAutoLoinView
    }

    fun goServerAutoLogin(){
        val accessToken = RequestRefreshTokenBody(USER_TOKEN)
        Log.d("accessToken",USER_TOKEN)
        RequestServer.userService.refreshToken(accessToken).enqueue(object :
            Callback<ResponseRefreshTokenBody>{
            override fun onResponse(
                call: Call<ResponseRefreshTokenBody>,
                response: Response<ResponseRefreshTokenBody>
            ) {
                if(response == null){
                    mAutoLoinView.onValidateFail(404);
                }else if(response.isSuccessful){
                    mAutoLoinView.onValidateSuccess(response.body()!!);
                }else{
                    mAutoLoinView.onValidateFail(400);
                }
            }

            override fun onFailure(call: Call<ResponseRefreshTokenBody>, t: Throwable) {
                mAutoLoinView.onValidateFail(404);
            }
        })
    }
}