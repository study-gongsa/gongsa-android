package com.app.gong4.DTO

data class RequestRefreshTokenBody(
    val refreshToken: String
)

data class ResponseRefreshTokenBody(
    var location : String,
    var msg : String,
    var data : AccessToken
)


data class AccessToken(
    var accessToken : String
)