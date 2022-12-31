package com.app.gong4.model.res

import com.app.gong4.model.UserCategory

data class ResponseUserCategory(
    val location: String,
    val msg: String,
    val data: List<UserCategory>,
)