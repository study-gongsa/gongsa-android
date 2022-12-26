package com.app.gong4.model

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
    val location: Any,
    val msg: String,
    val data: ResponseGroupUID
)

data class ResponseGroupUID(
    val groupUID: Int
)