package com.app.gong4.DTO

data class RequestEnterMember(
    val groupUID: Int
)

data class ResponseEnterMember(
    var location : String,
    var msg : String,
    var data : String
)
