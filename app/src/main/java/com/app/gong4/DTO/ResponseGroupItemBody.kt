package com.app.gong4.DTO

import java.sql.Timestamp

data class ResponseGroupItemBody(
    var location : String,
    var msg : String,
    var data : Data
)

data class Data(
    val studyGroupList: List<StduyGroupItem>
)

data class StduyGroupItem(
    var createdAt : Long,
    var expiredAt : Long,
    var name : String,
    var isCam: Boolean,
    var studyGroupUID : Int,
)

