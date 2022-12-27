package com.app.gong4.model.req

data class RequestGroupItemBody(
    var align:String?=null,
    var categoryUIDs:List<Int>,
    var isCam:Boolean?=null,
    var word:String?=null
)
