package com.app.gong4.model.res

import com.app.gong4.model.QnaItem

data class ResponseQnaListBody(
    val location: String,
    val msg: String,
    val data: QnaList,
){
    data class QnaList(
        val questionList: List<QnaItem>
    )
}

