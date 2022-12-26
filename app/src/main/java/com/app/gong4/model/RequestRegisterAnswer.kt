package com.app.gong4.model

data class RequestRegisterAnswer(
    val questionUID: Int,
    val content: String
)

data class ResponseRegisterAnswerBody(
    val location: String,
    val msg: String,
    val data: QuestionUID,
)

data class QuestionUID(
    val questionUID: Int
)