package com.app.gong4.api

import com.app.gong4.util.MainApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestServer {
    val BASE_URL = "http://3.36.170.161:8080"
    var USER_TOKEN = ""

    val networkInterceptor = Interceptor{ chain ->
        USER_TOKEN = MainApplication.prefs.getData("accessToken","")
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization","Bearer $USER_TOKEN")
            .build()
        val response = chain.proceed(newRequest)

        response.newBuilder().build()
    }

    val client = OkHttpClient.Builder().addNetworkInterceptor(networkInterceptor).build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    // 로그인, 회원가입 서비스
    val userService : UserService = retrofit.create(UserService::class.java)

    // 스터디 그룹 관련 함수
    val studyGroupService : StudyGroupService = retrofit.create(StudyGroupService::class.java)

    // 스터디 그룹 관련 함수
    val userCategoryService : UserCategoryService = retrofit.create(UserCategoryService::class.java)
}