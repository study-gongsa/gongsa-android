package com.app.gong4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.gong4.R
import com.app.gong4.api.UserService
import com.app.gong4.model.UserInfo
import com.app.gong4.model.req.RequestLoginBody
import com.app.gong4.model.req.RequestUserInfo
import com.app.gong4.model.res.BaseResponse
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.model.res.ResponseMyPageInfoBody
import com.app.gong4.model.res.ResponseUserBody
import com.app.gong4.utils.NetworkResult
import com.google.gson.Gson
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService){
    private val _loginRes = MutableLiveData<NetworkResult<ResponseLoginBody>>()
    val loginRes : LiveData<NetworkResult<ResponseLoginBody>>
        get() = _loginRes

    private val _userInfoRes = MutableLiveData<NetworkResult<UserInfo>>()
    val userInfoRes : LiveData<NetworkResult<UserInfo>>
        get() = _userInfoRes

    private val _userSettingRes = MutableLiveData<NetworkResult<ResponseUserBody.UserData>>()
    val userSettingRes : LiveData<NetworkResult<ResponseUserBody.UserData>>
        get() = _userSettingRes

    private val _patchSettingRes = MutableLiveData<NetworkResult<BaseResponse>>()
    val patchSettingRes : LiveData<NetworkResult<BaseResponse>>
        get() = _patchSettingRes

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

    suspend fun getUserInfo(){
        val response = userService.userMyPage()
        if(response.isSuccessful && response.body()!=null){
            _userInfoRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseMyPageInfoBody::class.java)
            _userInfoRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _loginRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }
    }

    suspend fun getSettingUserInfo(){
        val response = userService.getuserInfo()
        if(response.isSuccessful && response.body()!=null){
            _userSettingRes.postValue(NetworkResult.Success(response.body()!!.data))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseMyPageInfoBody::class.java)
            _userSettingRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _userSettingRes.value = NetworkResult.Error(null, R.string.server_error_msg)
        }
    }

    suspend fun patchUserInfo(image : MultipartBody.Part, requestUserInfo: RequestUserInfo){
        val response = userService.patchUserInfo(image,requestUserInfo)
        if(response.isSuccessful && response.body()!=null){
            _patchSettingRes.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseMyPageInfoBody::class.java)
            _userSettingRes.value = NetworkResult.Error(result.location,result.msg)
        }
    }
}