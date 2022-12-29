package com.app.gong4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.gong4.R
import com.app.gong4.api.UserService
import com.app.gong4.model.req.RequestLoginBody
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.utils.NetworkResult
import com.google.gson.Gson
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService){
    private val _loginRes = MutableLiveData<NetworkResult<ResponseLoginBody>>()
    val loginRes : LiveData<NetworkResult<ResponseLoginBody>>
        get() = _loginRes

    suspend fun login(loginReq:RequestLoginBody){
        val response = userService.login(loginReq)
        if(response.isSuccessful && response.body() !=null){
            _loginRes.value = NetworkResult.Success(response.body()!!)
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseLoginBody::class.java)
            _loginRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _loginRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }
    }
}