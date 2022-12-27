package com.app.gong4.model.res

import com.app.gong4.model.UserInfo

data class ResponseMyPageInfoBody(
    val location: String,
    val msg: String,
    val data: UserInfo,
)