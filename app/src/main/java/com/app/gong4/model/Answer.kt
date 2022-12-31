package com.app.gong4.model

data class Answer(
    val answer: String,
    val answerUID: Int,
    val createdAt: Long,
    val nickname: String,
    val userUID: Int
)
