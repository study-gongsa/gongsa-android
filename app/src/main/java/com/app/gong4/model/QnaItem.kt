package com.app.gong4.model


data class QnaItem(
    val questionUID: Int,
    val title: String,
    val content: String,
    val answerStatus: String,
    val createdAt: Long
)
