package com.app.gong4

import android.util.Log
import com.app.gong4.model.RequestRefreshTokenBody
import com.app.gong4.model.ResponseRefreshTokenBody
import com.app.gong4.api.RequestServer
import com.app.gong4.util.MainApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AutoLoginService {
    private var mAutoLoinView: AutoLoginView

    val USER_REFRESH_TOKEN = MainApplication.prefs.getData("refreshToken","")

    constructor(mAutoLoinView: AutoLoginView) {
        this.mAutoLoinView = mAutoLoinView
    }

    fun goServerAutoLogin(){
        val refreshToken = RequestRefreshTokenBody(USER_REFRESH_TOKEN)
        Log.d("refreshToken",USER_REFRESH_TOKEN)
        RequestServer.userService.refreshToken(refreshToken).enqueue(object :
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