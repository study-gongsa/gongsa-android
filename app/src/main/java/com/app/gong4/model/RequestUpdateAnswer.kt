package com.app.gong4.model

data class RequestUpdateAnswer(
    val answerUID: Int,
    val content: String
)

data class ResponseUpdateAnswerBody(
    val location: String,
    val msg: String,
    val data: QuestionUID,
)