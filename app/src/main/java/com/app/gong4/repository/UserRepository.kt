package com.app.gong4.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.gong4.R
import com.app.gong4.api.UserService
import com.app.gong4.model.Ranking
import com.app.gong4.model.ResponseCertifyEmailBody
import com.app.gong4.model.ResponseMyStudyGroupRankingBody
import com.app.gong4.model.UserInfo
import com.app.gong4.model.req.*
import com.app.gong4.model.res.*
import com.app.gong4.utils.NetworkResult
import com.app.gong4.utils.SingleLiveEvent
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService){
    private val _signUpRes = SingleLiveEvent<NetworkResult<BaseResponse>>()
    val signUpRes : LiveData<NetworkResult<BaseResponse>>
        get() = _signUpRes

    private val _loginRes = MutableLiveData<NetworkResult<ResponseLoginBody>>()
    val loginRes : LiveData<NetworkResult<ResponseLoginBody>>
        get() = _loginRes

    private val _loginRefreshRes = MutableLiveData<NetworkResult<ResponseRefreshTokenBody>>()
    val loginRefreshRes : LiveData<NetworkResult<ResponseRefreshTokenBody>>
        get() = _loginRefreshRes

    private val _userInfoRes = MutableLiveData<NetworkResult<UserInfo>>()
    val userInfoRes : LiveData<NetworkResult<UserInfo>>
        get() = _userInfoRes

    private val _userSettingRes = MutableLiveData<NetworkResult<ResponseUserBody.UserData>>()
    val userSettingRes : LiveData<NetworkResult<ResponseUserBody.UserData>>
        get() = _userSettingRes

    private val _patchSettingRes = MutableLiveData<NetworkResult<BaseResponse>>()
    val patchSettingRes : LiveData<NetworkResult<BaseResponse>>
        get() = _patchSettingRes

    private val _groupRankingList = MutableLiveData<NetworkResult<List<Ranking>>>()
    val groupRankingList : LiveData<NetworkResult<List<Ranking>>>
        get() = _groupRankingList

    private val _findPasswordRes = MutableLiveData<NetworkResult<BaseResponse>>()
    val findPasswordRes : LiveData<NetworkResult<BaseResponse>>
        get() = _findPasswordRes

    private val _certifyEmailRes = SingleLiveEvent<NetworkResult<BaseResponse>>()
    val certifyEmailRes : LiveData<NetworkResult<BaseResponse>>
        get() = _certifyEmailRes

    private val _confirmCodeRes = SingleLiveEvent<NetworkResult<BaseResponse>>()
    val confirmCodeRes : LiveData<NetworkResult<BaseResponse>>
        get() = _confirmCodeRes

    suspend fun refreshToken(refreshTokenBody:RequestRefreshTokenBody){
        val response = userService.refreshToken(refreshTokenBody)
        if(response.isSuccessful){
            _loginRefreshRes.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.body()!!.msg!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseLoginBody::class.java)
            _loginRefreshRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _loginRefreshRes.value = NetworkResult.NotConnect()
        }
    }

    suspend fun signup(signupRes:RequestSignupBody){
        val response = userService.signup(signupRes)
        if(response.isSuccessful && response.body() !=null){
            _signUpRes.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseLoginBody::class.java)
            _signUpRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _signUpRes.value = NetworkResult.NotConnect()
        }
    }

    suspend fun login(loginReq:RequestLoginBody){
        val response = userService.login(loginReq)
        if(response.isSuccessful && response.body() !=null){
            _loginRes.value = NetworkResult.Success(response.body()!!)
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseLoginBody::class.java)
            _loginRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _loginRes.value = NetworkResult.NotConnect()
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

    suspend fun patchUserInfo(image : MultipartBody.Part?=null, requestUserInfo: RequestUserInfo){
        val response = userService.patchUserInfo(image,requestUserInfo)
        if(response.isSuccessful && response.body()!=null){
            _patchSettingRes.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseMyPageInfoBody::class.java)
            _userSettingRes.value = NetworkResult.Error(result.location,result.msg)
        }
    }

    fun myStudyGroupRanking(){
        userService.myStudyGroupRanking().enqueue(object :
            Callback<ResponseMyStudyGroupRankingBody> {
            override fun onResponse(
                call: Call<ResponseMyStudyGroupRankingBody>,
                response: Response<ResponseMyStudyGroupRankingBody>
            ) {
                if(response.isSuccessful){
                    _groupRankingList.postValue(NetworkResult.Success(response.body()!!.data.groupRankList))
                }else{
                    _groupRankingList.postValue(NetworkResult.Error(response.body()!!.location,response.body()!!.msg))
                }
            }

            override fun onFailure(call: Call<ResponseMyStudyGroupRankingBody>, t: Throwable) {

            }
        })
    }

    fun findPassword(findPwdBody: RequestFindPwdBody){
        userService.findPwd(findPwdBody).enqueue(object :
            Callback<BaseResponse>{
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                if(response.isSuccessful){
                    _findPasswordRes.postValue(NetworkResult.ResultEmpty())
                }else{
                    val error = response.errorBody()!!.string().trimIndent()
                    val result = Gson().fromJson(error, BaseResponse::class.java)
                    _findPasswordRes.value = NetworkResult.Error(result.location,result.msg)
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                _findPasswordRes.postValue(NetworkResult.NotConnect())
            }

        })
    }

    suspend fun certifyEmail(emailBody: RequestCertifyEmailBody){
        val response = userService.certifyEmail(emailBody)
        _certifyEmailRes.postValue(NetworkResult.ResultEmpty())
    }

    suspend fun confirmCode(authCodeBodyReq:RequestAuthCodeBody){
        val response = userService.confirmCode(authCodeBodyReq)
        if(response.isSuccessful){
            _confirmCodeRes.postValue(NetworkResult.ResultEmpty())
        }else if(response.body()!!.msg!=null){
            val error = response.errorBody()!!.string().trimIndent()
            val result = Gson().fromJson(error, ResponseCertifyEmailBody::class.java)

            _confirmCodeRes.value = NetworkResult.Error(result.location,result.msg)
        }else{
            _confirmCodeRes.value = NetworkResult.NotConnect()
        }
    }
}