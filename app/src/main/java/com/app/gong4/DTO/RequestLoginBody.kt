package com.app.gong4.DTO

data class RequestLoginBody(
    var email : String,
    var passwd : String
)

data class ResponseLoginBody(
    var location : String,
    var msg : String,
    var data : Token
)