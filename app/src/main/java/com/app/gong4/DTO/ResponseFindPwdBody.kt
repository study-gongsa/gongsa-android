package com.app.gong4.DTO

data class RequestFindPwdBody(
    var email : String
)
data class ResponseFindPwdBody(
    var location : String,
    var msg : String,
    var data : String
)
