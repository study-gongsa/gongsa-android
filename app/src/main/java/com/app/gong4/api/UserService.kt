package com.app.gong4.api

import com.app.gong4.DTO.RequestLoginBody
import com.app.gong4.DTO.ResponseLoginBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/api/user/login")
    fun login(@Body body: RequestLoginBody) : Call<ResponseLoginBody>
}