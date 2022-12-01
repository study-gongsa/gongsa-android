package com.app.gong4.api

import com.app.gong4.RequestSaveUserCateogry
import com.app.gong4.ResponseSaveUserCategory
import com.app.gong4.ResponseUserCategory
import retrofit2.Call
import retrofit2.http.*

interface UserCategoryService {
    /* 사용자 카테고리 조회 */
    @GET("/api/user-category")
    fun getUserCategory() : Call<ResponseUserCategory>

    /* 사용자 카테고리 저장 */
    @PUT("/api/user-category")
    fun putUserCategory(@Body requestSaveUserCateogry: RequestSaveUserCateogry) : Call<ResponseSaveUserCategory>
}