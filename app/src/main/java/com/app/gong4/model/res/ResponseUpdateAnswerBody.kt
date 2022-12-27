package com.app.gong4.model.res

import com.app.gong4.model.QuestionUID

data class ResponseUpdateAnswerBody(
    val location: String,
    val msg: String,
    val data: QuestionUID,
)