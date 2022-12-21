package com.app.gong4.DTO

data class RequestUpdateAnswer(
    val answerUID: Int,
    val content: String
)

data class ResponseUpdateAnswerBody(
    val location: String,
    val msg: String,
    val data: QuestionUID,
)