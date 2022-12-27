package com.app.gong4.model.req

data class RequestUserInfo(
    val nickname: String,
    var passwd : String? = null,
    val changeImage: Boolean
)
