package com.app.gong4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gong4.model.ResponseCertifyEmailBody
import com.app.gong4.model.UserInfo
import com.app.gong4.model.req.*
import com.app.gong4.model.res.*
import com.app.gong4.repository.UserRepository
import com.app.gong4.utils.NetworkResult
import com.app.gong4.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){
    val signupRes : LiveData<NetworkResult<BaseResponse>>
        get() = userRepository.signUpRes
    val loginRes : LiveData<NetworkResult<ResponseLoginBody>>
        get() = userRepository.loginRes

    val userInfoRes : LiveData<NetworkResult<UserInfo>>
        get() = userRepository.userInfoRes
    val userSettingInfoRes : LiveData<NetworkResult<ResponseUserBody.UserData>>
        get() = userRepository.userSettingRes
    val patchUserSettingInfoRes : LiveData<NetworkResult<BaseResponse>>
        get() = userRepository.patchSettingRes
    val loginRefreshRes : LiveData<NetworkResult<ResponseRefreshTokenBody>>
        get() = userRepository.loginRefreshRes
    val findPasswordRes : LiveData<NetworkResult<BaseResponse>>
        get() = userRepository.findPasswordRes

    val certifyEmailRes : LiveData<NetworkResult<BaseResponse>>
        get() = userRepository.certifyEmailRes

    val confirmCodeRes : LiveData<NetworkResult<BaseResponse>>
        get() = userRepository.confirmCodeRes

    val rankListLiveData get() = userRepository.groupRankingList

    fun loginUser(loginReq : RequestLoginBody){
        viewModelScope.launch {
            userRepository.login(loginReq)
        }
    }

    fun getUserInfo(){
        viewModelScope.launch{
            userRepository.getUserInfo()
        }
    }

    fun getSettingUserInfo(){
        viewModelScope.launch {
            userRepository.getSettingUserInfo()
        }
    }

    fun patchUserInfo(image : MultipartBody.Part, requestUserInfo: RequestUserInfo){
        viewModelScope.launch {
            userRepository.patchUserInfo(image,requestUserInfo)
        }
    }

    fun myStudyGroupRanking(){
        viewModelScope.launch {
            userRepository.myStudyGroupRanking()
        }
    }

    fun refreshToken(refreshTokenBody: RequestRefreshTokenBody){
        viewModelScope.launch {
            userRepository.refreshToken(refreshTokenBody)
        }
    }

    fun findPassword(findPwdBody: RequestFindPwdBody){
        viewModelScope.launch {
            userRepository.findPassword(findPwdBody)
        }
    }

    fun certifyEmail(emailBody: RequestCertifyEmailBody){
        viewModelScope.launch {
            userRepository.certifyEmail(emailBody)
        }
    }

    fun confirmCode(authCodeBodyReq:RequestAuthCodeBody){
        viewModelScope.launch {
            userRepository.confirmCode(authCodeBodyReq)
        }
    }

    fun signUp(signupRes:RequestSignupBody){
        viewModelScope.launch {
            userRepository.signup(signupRes)
        }
    }
}