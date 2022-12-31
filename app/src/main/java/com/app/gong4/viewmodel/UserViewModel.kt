package com.app.gong4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gong4.model.UserInfo
import com.app.gong4.model.req.RequestLoginBody
import com.app.gong4.model.req.RequestUserInfo
import com.app.gong4.model.res.BaseResponse
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.model.res.ResponseMyPageInfoBody
import com.app.gong4.model.res.ResponseUserBody
import com.app.gong4.repository.UserRepository
import com.app.gong4.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){
    val loginRes : LiveData<NetworkResult<ResponseLoginBody>>
        get() = userRepository.loginRes
    val userInfoRes : LiveData<NetworkResult<UserInfo>>
        get() = userRepository.userInfoRes
    val userSettingInfoRes : LiveData<NetworkResult<ResponseUserBody.UserData>>
        get() = userRepository.userSettingRes
    val patchUserSettingInfoRes : LiveData<NetworkResult<BaseResponse>>
    get() = userRepository.patchSettingRes

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
}