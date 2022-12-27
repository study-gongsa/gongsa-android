package com.app.gong4.repository

import com.app.gong4.api.UserService
import com.app.gong4.model.req.RequestLoginBody
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService){
    suspend fun login(email:String, passwd : String) = userService.login(RequestLoginBody(email,passwd))
}