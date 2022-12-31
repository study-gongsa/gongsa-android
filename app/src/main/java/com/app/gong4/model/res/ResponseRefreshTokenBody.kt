package com.app.gong4.model.res

data class ResponseRefreshTokenBody(
    var location : String,
    var msg : String,
    var data : AccessToken
){
    data class AccessToken(
        var accessToken : String
    )
}
