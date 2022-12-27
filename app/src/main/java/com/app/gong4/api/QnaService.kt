package com.app.gong4.api

import com.app.gong4.model.req.RequestRegisterAnswer
import com.app.gong4.model.req.RequestUpdateAnswer
import com.app.gong4.model.res.ResponseQnaListBody
import com.app.gong4.model.res.ResponseQuestionBody
import com.app.gong4.model.res.ResponseRegisterAnswerBody
import com.app.gong4.model.res.ResponseUpdateAnswerBody
import retrofit2.Call
import retrofit2.http.*

interface QnaService {
    //스터디그룹 질문 모아보기
    @GET("/api/question/group-question/{groupUID}")
    fun getQnaList(@Path("groupUID")groupUID:Int) : Call<ResponseQnaListBody>

    //질문 상세보기
    @GET("/api/question/{questionUID}")
    fun getQusetionDetail(@Path("questionUID")questionUID:Int) : Call<ResponseQuestionBody>

    //답변 등록
    @POST("/api/answer")
    fun postAnswer(@Body body: RequestRegisterAnswer) : Call<ResponseRegisterAnswerBody>

    //답변 삭제
    @DELETE("/api/answer/{answerUID}")
    fun deleteAnswer(@Path("answerUID")answerUID:Int) : Call<Void>

    //답변 수정
    @PATCH("/api/answer")
    fun patchAnswer(@Body body: RequestUpdateAnswer) : Call<ResponseUpdateAnswerBody>

    //내 질문 모아보기
    @GET("/api/question/my-question")
    fun getMyQnaList():Call<ResponseQnaListBody>
}