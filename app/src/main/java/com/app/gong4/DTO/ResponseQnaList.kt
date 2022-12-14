package com.app.gong4.DTO

data class ResponseQnaList(
    val location: String,
    val msg: String,
    val data: QnaList,
)

data class QnaList(
    val questionList: List<QnaItem>
)

data class QnaItem(
    val questionUID: Int,
    val title: String,
    val content: String,
    val answerStatus: String,
    val createdAt: Long
)