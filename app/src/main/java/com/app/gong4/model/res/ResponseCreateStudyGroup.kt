package com.app.gong4.model.res

data class ResponseCreateStudyGroup (
    val location: Any,
    val msg: String,
    val data: ResponseGroupUID
){
    data class ResponseGroupUID(
        val groupUID: Int
    )
}
