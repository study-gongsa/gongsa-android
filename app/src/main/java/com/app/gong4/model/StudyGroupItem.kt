package com.app.gong4.model

data class StudyGroupItem(
    var createdAt : Long,
    var expiredAt : Long,
    var name : String,
    var isCam: Boolean,
    var imgPath : String,
    var studyGroupUID : Int,
)
