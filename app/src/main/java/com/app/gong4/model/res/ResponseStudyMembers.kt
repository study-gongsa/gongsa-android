package com.app.gong4.model.res

import com.app.gong4.model.Member

data class ResponseStudyMembers (
    val location: String,
    val msg: String,
    val data: StudyMembers
){
    data class StudyMembers (
        var maxMembers: Int,
        var members: List<Member>
    )
}