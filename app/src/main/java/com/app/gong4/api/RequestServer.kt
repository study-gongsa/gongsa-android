package com.app.gong4.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestServer {
    val BASE_URL = "http://3.36.170.161:8080"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 로그인, 회원가입 서비스
    val userService : UserService = retrofit.create(UserService::class.java)
}