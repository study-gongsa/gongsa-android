package com.app.gong4.api

import com.app.gong4.DTO.ResponseGroupItemBody
import com.app.gong4.DTO.ResponseStudycategoryBody
import com.app.gong4.DTO.ResponseStudygroupinfoBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StudyGroupService {
    @GET("/api/study-group/recommend")
    fun recommend(@Query("groupUID")groupUID : Int?=null, @Query("type")type : String?=null) : Call<ResponseGroupItemBody>

    @GET("/api/category")
    fun getCategory() : Call<ResponseStudycategoryBody>

    @GET("/api/study-group/{groupUID}")
    fun getStudygroupInfo(@Path("groupUID")groupUID:Int) : Call<ResponseStudygroupinfoBody>

    @GET("/api/study-group/search")
    fun getStudygroupfilterInfo(
        @Query("align")align:String,
        @Query(value = "categoryUIDs",encoded = true)categoryUIDs:List<Int>,
        @Query("isCam")isCam:Boolean,
        @Query("word")word:String,
    ) : Call<ResponseStudygroupinfoBody>

    @GET("/api/study-group/code/{code}")
    fun getStudygroupCodeInfo(@Path("code")code:String) : Call<ResponseStudygroupinfoBody>
}