package com.app.gong4.api

import com.app.gong4.DTO.ResponseGroupItemBody
import com.app.gong4.DTO.ResponseStudycategoryBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StudyGroupService {
    @GET("/api/study-group/recommend")
    fun recommend(@Query("groupUID")groupUID : Int?=null, @Query("type")type : String?=null) : Call<ResponseGroupItemBody>

    @GET("/api/category")
    fun getCategory() : Call<ResponseStudycategoryBody>
}