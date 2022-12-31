package com.app.gong4.model.res

import com.app.gong4.model.QuestionUID

data class ResponseRegisterAnswerBody(
    val location: String,
    val msg: String,
    val data: QuestionUID,
)
