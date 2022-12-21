package com.app.gong4.DTO

data class ResponseMyPageInfoBody(
    val location: String,
    val msg: String,
    val data: UserInfo,
)

data class UserInfo(
    val imgPath: String,
    val level: Int,
    val nickname: String,
    val percentage: Double,
    val totalStudyTime: String
)