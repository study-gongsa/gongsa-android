package com.app.gong4

import com.app.gong4.model.res.ResponseRefreshTokenBody
import com.app.gong4.api.RequestServer
import com.app.gong4.model.req.RequestRefreshTokenBody
import com.app.gong4.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AutoLoginService {
    private var mAutoLoinView: AutoLoginView

    @Inject
    lateinit var tokenManager: TokenManager

    private val RefreshToken = tokenManager.getRefreshToken()!!

    constructor(mAutoLoinView: AutoLoginView) {
        this.mAutoLoinView = mAutoLoinView
    }

    fun goServerAutoLogin(){
        val refreshToken = RequestRefreshTokenBody(RefreshToken)

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