package com.app.gong4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.gong4.model.req.RequestLoginBody
import com.app.gong4.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel(){
    private val _response = MutableLiveData<RequestLoginBody>()
    val _loginResponse : LiveData<RequestLoginBody>
        get() = _response

}