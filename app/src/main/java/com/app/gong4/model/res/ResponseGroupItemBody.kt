package com.app.gong4.model.res

import com.app.gong4.model.StudyGroupItem

data class ResponseGroupItemBody(
    var location : String,
    var msg : String,
    var data : StudyGroupList
){
    data class StudyGroupList(
        val studyGroupList: List<StudyGroupItem>
    )
}



