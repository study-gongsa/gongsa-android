package com.app.gong4.model.req

data class RequestCreateStudyGroup(
    val categoryUIDs: List<Int>,
    val expiredAt: String,
    val isCam: Boolean,
    val isPenalty: Boolean,
    val isPrivate: Boolean,
    var maxPenalty : Int?=null,
    val maxMember: Int,
    val maxTodayStudy: Int,
    val minStudyHour: Int,
    val name: String
)
