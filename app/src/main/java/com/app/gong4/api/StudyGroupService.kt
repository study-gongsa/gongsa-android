package com.app.gong4.api

import com.app.gong4.model.req.RequestEnterMember
import com.app.gong4.model.res.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface StudyGroupService {
    @GET("/api/study-group/recommend")
    fun recommend(@Query("groupUID")groupUID : Int?=null, @Query("type")type : String?=null) : Call<ResponseGroupItemBody>

    @GET("/api/category")
    fun getCategory() : Call<ResponseStudycategoryBody>

    @GET("/api/study-group/{groupUID}")
    fun getStudygroupInfo(@Path("groupUID")groupUID:Int) : Call<ResponseStudygroupinfoBody>

    @GET("/api/study-group/search")
    fun getStudygroupfilterInfo(
        @Query("align") align:String?=null,
        @Query("categoryUIDs") categoryUIDs: List<Int>? =null,
        @Query("isCam") isCam:Boolean?=null,
        @Query("word") word:String?=null,
    ) : Call<ResponseGroupItemBody>

    @GET("/api/study-group/code/{code}")
    fun getStudygroupCodeInfo(@Path("code")code:String) : Call<ResponseStudygroupinfoBody>

    @POST("/api/group-member")
    fun getStudyEnter(@Body body: RequestEnterMember) : Call<ResponseEnterMember>

    //나의 스터디 그룹 조회
    @GET("/api/study-group/my-group")
    fun getMyStudyGroup() : Call<ResponseGroupItemBody>

    //스터디 그룹 내 멤버 정보 조회
    @GET("/api/group-member/{groupUID}")
    fun getStudyMembers(@Path("groupUID")groupUID:Int) : Call<ResponseStudyMembers>

    @Multipart
    @Headers("accept: application/json")
    @POST("/api/study-group")
    fun createStudygroup(
        @Part image:MultipartBody.Part,
        @Part("json") body: Any
    ) : Call<ResponseCreateStudyGroup>
}