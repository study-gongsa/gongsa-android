package com.app.gong4.fragments

import com.app.gong4.AutoLoginView
import com.app.gong4.MainApplication
import com.app.gong4.model.res.ResponseRefreshTokenBody
import com.app.gong4.api.RequestServer
import com.app.gong4.model.req.RequestRefreshTokenBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AutoLoginService {
    private var mAutoLoinView: AutoLoginView

    constructor(mAutoLoinView: AutoLoginView) {
        this.mAutoLoinView = mAutoLoinView
    }

    fun goServerAutoLogin(){
        val refreshToken = RequestRefreshTokenBody(MainApplication.tokenManager.getRefreshToken()!!)

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