package com.app.gong4.model

data class ResponseUserBody (
    val location: Any,
    val msg: Any,
    val data: UserData,
){
    data class UserData(
        val imgPath : String,
        val nickname : String
    )
}