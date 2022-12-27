package com.app.gong4.model

data class Member(
    var userUID: Int,
    var nickname: String,
    var imgPath: String,
    var studyStatus: String,
    var totalStudyTime: String,
    var ranking: Int
)
