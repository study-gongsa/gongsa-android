package com.app.gong4.api

import com.app.gong4.model.req.RequestEnterMember
import com.app.gong4.model.res.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface StudyGroupService {
    @GET("/api/study-group/recommend")
    suspend fun recommend(@Query("groupUID")groupUID : Int?=null, @Query("type")type : String?=null) : Response<ResponseGroupItemBody>

    @GET("/api/study-group/{groupUID}")
    suspend fun getStudygroupInfo(@Path("groupUID")groupUID:Int) : Response<ResponseStudygroupinfoBody>

    @GET("/api/study-group/search")
    fun getStudygroupfilterInfo(
        @Query("align") align:String?=null,
        @Query("categoryUIDs") categoryUIDs: List<Int>? =null,
        @Query("isCam") isCam:Boolean?=null,
        @Query("word") word:String?=null,
    ) : Call<ResponseGroupItemBody>

    @GET("/api/study-group/code/{code}")
    suspend fun getStudygroupCodeInfo(@Path("code")code:String) : Response<ResponseStudygroupinfoBody>

    @POST("/api/group-member")
    suspend fun getStudyEnter(@Body body: RequestEnterMember) : Response<ResponseEnterMember>

    //나의 스터디 그룹 조회
    @GET("/api/study-group/my-group")
    suspend fun getMyStudyGroup() : Response<ResponseGroupItemBody>

    //스터디 그룹 내 멤버 정보 조회
    @GET("/api/group-member/{groupUID}")
    suspend fun getStudyMembers(@Path("groupUID")groupUID:Int) : Response<ResponseStudyMembers>

    @Multipart
    @Headers("accept: application/json")
    @POST("/api/study-group")
    fun createStudygroup(
        @Part image:MultipartBody.Part,
        @Part("json") body: Any
    ) : Call<ResponseCreateStudyGroup>

    // 스터디 그룹 탈퇴
    @DELETE("/api/group-member/{groupUID}")
    suspend fun leaveGroup(@Path("groupUID")groupUID:Int) : Response<BaseResponse>
}