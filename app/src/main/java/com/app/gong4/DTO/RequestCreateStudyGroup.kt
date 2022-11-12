package com.app.gong4.DTO

data class RequestCreateStudyGroup(
    val categoryUIDs: List<Int>,
    val expiredAt: String,
    val isCam: Boolean,
    val isPenalty: Boolean,
    val isPrivate: Boolean,
    val maxPenalty : Int,
    val maxMember: Int,
    val maxTodayStudy: Int,
    val minStudyHour: Int,
    val name: String
)

data class ResponseCreateStudyGroup (
    val data: ResponseGroupUID,
    val location: String,
    val msg: String
    )

data class ResponseGroupUID(
    val groupUID: Int
)