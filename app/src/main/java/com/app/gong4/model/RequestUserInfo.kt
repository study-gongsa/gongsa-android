package com.app.gong4.model

data class RequestUserInfo(
    val nickname: String,
    var passwd : String? = null,
    val changeImage: Boolean
)
