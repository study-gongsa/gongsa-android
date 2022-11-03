package com.app.gong4.api

import com.app.gong4.DTO.*
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
    fun getStudyEnter(@Body body:RequestEnterMember) : Call<ResponseEnterMember>
}