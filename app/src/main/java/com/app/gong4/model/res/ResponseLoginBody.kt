package com.app.gong4.model.res

data class ResponseLoginBody(
    var location : String,
    var msg : String,
    var data : Token
){
    data class Token(
        var accessToken : String,
        var refreshToken : String
    )
}


