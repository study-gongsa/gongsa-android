package com.app.gong4.api

import com.app.gong4.model.*
import com.app.gong4.model.req.*
import com.app.gong4.model.res.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    /* 로그인 */
    @POST("/api/user/login")
    suspend fun login(@Body body: RequestLoginBody) : Response<ResponseLoginBody>

    /* refresh token */
    @POST("/api/user/login/refresh")
    suspend fun refreshToken(@Body body: RequestRefreshTokenBody) : Response<ResponseRefreshTokenBody>

    /* 회원가임 */
    @POST("/api/user/join")
    suspend fun signup(@Body body: RequestSignupBody) : Response<BaseResponse>

    /* 이메일 인증 */
    @PATCH("/api/user/mail/join")
    suspend fun certifyEmail(@Body body: RequestCertifyEmailBody) : Response<BaseResponse>

    @PATCH("api/user/code")
    suspend fun confirmCode(@Body body: RequestAuthCodeBody) : Response<BaseResponse>

    /* 비밀번호 찾기 */
    @PATCH("/api/user/mail/passwd")
    fun findPwd(@Body body: RequestFindPwdBody) : Call<BaseResponse>

    /* 환경설정 - 유저정보조회 */
    @GET("/api/user/mypage")
    suspend fun userMyPage() : Response<ResponseMyPageInfoBody>

    /* 내 스터디그룹 랭킹 조회 */
    @GET("/api/study-group/my-ranking")
    fun myStudyGroupRanking() : Call<ResponseMyStudyGroupRankingBody>

    /* 특정 기기로 푸시 알림 전송되는지 테스트 */
    @GET("/api/push")
    fun userPushMessage(@Query("targetToken") targetToken:String) : Call<BaseResponse>

    /* 환경설정 기본 정보 조회*/
    @GET("/api/user")
    suspend fun getuserInfo():Response<ResponseUserBody>

    @Multipart
    @Headers("accept: application/json")
    @PATCH("/api/user")
    suspend fun patchUserInfo(
        @Part image: MultipartBody.Part?=null,
        @Part("json") body: Any
    ):Response<BaseResponse>
}