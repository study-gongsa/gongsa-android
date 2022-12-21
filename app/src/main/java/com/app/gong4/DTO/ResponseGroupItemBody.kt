package com.app.gong4.DTO

import retrofit2.http.Query
import java.sql.Timestamp
import java.util.*

data class RequestGroupItemBody(
    var align:String?=null,
    var categoryUIDs:List<Int>,
    var isCam:Boolean?=null,
    var word:String?=null
)
data class ResponseGroupItemBody(
    var location : String,
    var msg : String,
    var data : StudyGroupList
)

data class StudyGroupList(
    val studyGroupList: List<StduyGroupItem>
)

data class StduyGroupItem(
    var createdAt : Long,
    var expiredAt : Long,
    var name : String,
    var isCam: Boolean,
    var imgPath : String,
    var studyGroupUID : Int,
)

