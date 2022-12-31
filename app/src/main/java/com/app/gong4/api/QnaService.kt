package com.app.gong4.api

import com.app.gong4.model.req.RequestQuestion
import com.app.gong4.model.req.RequestRegisterAnswer
import com.app.gong4.model.req.RequestUpdateAnswer
import com.app.gong4.model.res.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface QnaService {
    //스터디그룹 질문 모아보기
    @GET("/api/question/group-question/{groupUID}")
    suspend fun getQnaList(@Path("groupUID")groupUID:Int) : Response<ResponseQnaListBody>

    //질문 상세보기
    @GET("/api/question/{questionUID}")
    suspend fun getQusetionDetail(@Path("questionUID")questionUID:Int) : Response<ResponseQuestionBody>

    //답변 등록
    @POST("/api/answer")
    suspend fun postAnswer(@Body body: RequestRegisterAnswer) : Response<ResponseRegisterAnswerBody>

    //답변 삭제
    @DELETE("/api/answer/{answerUID}")
    fun deleteAnswer(@Path("answerUID")answerUID:Int) : Call<BaseResponse>

    //답변 수정
    @PATCH("/api/answer")
    suspend fun patchAnswer(@Body body: RequestUpdateAnswer) : Response<ResponseUpdateAnswerBody>

    //내 질문 모아보기
    @GET("/api/question/my-question")
    suspend fun getMyQnaList():Response<ResponseQnaListBody>

    // 질문 등록
    @POST("/api/question")
    suspend fun saveQuestion(@Body body : RequestQuestion) : Response<ResponseQuestion>
}