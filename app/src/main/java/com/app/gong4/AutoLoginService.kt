package com.app.gong4

import android.util.Log
import com.app.gong4.model.res.ResponseRefreshTokenBody
import com.app.gong4.api.RequestServer
import com.app.gong4.model.req.RequestRefreshTokenBody
import com.app.gong4.utils.TokenManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@EntryPoint
@InstallIn(SingletonComponent::class)
class AutoLoginService {
    private var mAutoLoinView: AutoLoginView
    var tokenManager: TokenManager

    constructor(mAutoLoinView: AutoLoginView,tokenManager: TokenManager) {
        this.mAutoLoinView = mAutoLoinView
        this.tokenManager = tokenManager
    }

    fun goServerAutoLogin(){
        val refreshToken = RequestRefreshTokenBody(tokenManager.getRefreshToken()!!)

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