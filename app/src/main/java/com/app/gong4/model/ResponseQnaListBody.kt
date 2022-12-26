package com.app.gong4.model

data class ResponseQnaListBody(
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