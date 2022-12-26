package com.app.gong4.model

data class RequestSignupBody(
    var email : String,
    var nickname: String,
    var passwd: String
)

data class ResponseSignupBody(
    var location : String,
    var msg : String,
    var data : String
)