package com.app.gong4.model

data class ResponseStudyMembers (
    val location: Any,
    val msg: Any,
    val data: StudyMembers
)

data class StudyMembers (
    var maxMembers: Int,
    var members: List<Member>
)

data class Member(
    var userUID: Int,
    var nickname: String,
    var imgPath: String,
    var studyStatus: String,
    var totalStudyTime: String,
    var ranking: Int
)