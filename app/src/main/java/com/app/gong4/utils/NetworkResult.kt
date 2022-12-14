package com.app.gong4.utils

sealed class NetworkResult<T>(val data: T? =null,val location:Any?=null, val msg:Any?=null){
    class Success<T>(data: T) : NetworkResult<T>(data,null,null)
    class ResultEmpty<T>():NetworkResult<T>()
    class Error<T>(location: Any?=null,msg: Any?=null):NetworkResult<T>(null,location,msg)
    class Loading<T> : NetworkResult<T>()
    class NotConnect<T>:NetworkResult<T>(null,null,"서버와의 통신이 원활하지 않습니다")
}
