package com.app.gong4.DTO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ResponseMyPageInfoBody(
    val location: String,
    val msg: String,
    val data: UserInfo,
)

@Parcelize
data class UserInfo(
    val imgPath: String,
    val level: Int,
    val nickname: String,
    val percentage: Double,
    val totalStudyTime: String
) : Parcelable