package com.app.gong4.api

import com.app.gong4.DTO.RequestFindPwdBody
import com.app.gong4.DTO.RequestLoginBody
import com.app.gong4.DTO.RequestSignupBody
import com.app.gong4.DTO.ResponseFindPwdBody
import com.app.gong4.DTO.ResponseLoginBody
import com.app.gong4.DTO.ResponseSignupBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    /* 로그인 */
    @POST("/api/user/login")
    fun login(@Body body: RequestLoginBody) : Call<ResponseLoginBody>


    @POST("/api/user/join")
    fun signup(@Body body: RequestSignupBody) : Call<ResponseSignupBody>

    /* 비밀번호 찾기 */
    @PATCH("/api/user/mail/passwd")
    fun findPwd(@Body body: RequestFindPwdBody) : Call<ResponseFindPwdBody>
}