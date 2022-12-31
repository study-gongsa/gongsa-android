package com.app.gong4.api

import com.app.gong4.model.res.ResponseUserCategory
import com.app.gong4.model.req.RequestSaveUserCateogry
import com.app.gong4.model.res.BaseResponse
import com.app.gong4.model.res.ResponseStudycategoryBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserCategoryService {
    /* 카테고리 종류 조회 */
    @GET("/api/category")
    suspend fun getCategory() : Response<ResponseStudycategoryBody>

    /* 사용자 카테고리 조회 */
    @GET("/api/user-category")
    suspend fun getUserCategory() : Response<ResponseUserCategory>

    /* 사용자 카테고리 저장 */
    @PUT("/api/user-category")
    suspend fun putUserCategory(@Body requestSaveUserCateogry: RequestSaveUserCateogry) : Response<BaseResponse>
}