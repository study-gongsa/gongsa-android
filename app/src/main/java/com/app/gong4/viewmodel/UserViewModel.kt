package com.app.gong4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gong4.model.req.RequestLoginBody
import com.app.gong4.model.res.ResponseLoginBody
import com.app.gong4.repository.UserRepository
import com.app.gong4.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel(){
    val loginRes : LiveData<NetworkResult<ResponseLoginBody>>
        get() = userRepository.loginRes

    fun loginUser(loginReq : RequestLoginBody){
        viewModelScope.launch {
            userRepository.login(loginReq)
        }
    }
}