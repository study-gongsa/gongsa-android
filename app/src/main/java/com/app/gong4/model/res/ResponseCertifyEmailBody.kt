package com.app.gong4.model

data class ResponseCertifyEmailBody(
    var location : String,
    var msg : String,
    var data : String
)

data class ResponseAuthCodeBody(
    var location : String,
    var msg : String,
    var data : String
)