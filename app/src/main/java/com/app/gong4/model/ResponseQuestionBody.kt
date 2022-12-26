package com.app.gong4.model

data class ResponseQuestionBody(
    val location: String,
    val msg: String,
    val data: Question,
)

data class Question(
    val title: String,
    val content: String,
    val answerList: List<Answer>
)

data class Answer(
    val answer: String,
    val answerUID: Int,
    val createdAt: Long,
    val nickname: String,
    val userUID: Int
)