package com.app.gong4.DTO

data class RequestCertifyEmailBody(
    var email : String
)
data class ResponseCertifyEmailBody(
    var location : String,
    var msg : String,
    var data : String
)

data class RequestAuthCodeBody(
    var authCode: String,
    var email: String
)
data class ResponseAuthCodeBody(
    var location : String,
    var msg : String,
    var data : String
)