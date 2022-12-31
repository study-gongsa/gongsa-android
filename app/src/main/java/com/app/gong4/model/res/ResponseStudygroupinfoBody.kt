package com.app.gong4.model.res

import com.app.gong4.model.StudyCategory

data class ResponseStudygroupinfoBody(
    val location: Any,
    val msg: Any,
    val data: StudyGroupDetailItem
){
    data class StudyGroupDetailItem(
        var createdAt : Long,
        var expiredAt : Long,
        var name : String,
        var isCam: Boolean,
        var maxMember :Int,
        var currentMember : Int,
        var groupUID : Int,
        var code : String,
        var minStudyHour : String,
        var categories : List<StudyCategory>
    )
}
