package com.app.gong4.model.res

import com.app.gong4.model.Answer

data class ResponseQuestionBody(
    val location: String,
    val msg: String,
    val data: Question,
){
    data class Question(
        val title: String,
        val content: String,
        val answerList: List<Answer>
    )
}