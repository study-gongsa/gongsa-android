package com.app.gong4.utils

sealed class NetworkResult<T>(val data: T? =null,val location:Any?=null, val msg:Any?=null){
    class Success<T>(data : T) : NetworkResult<T>(data,null,null)
    class Error<T>(location: Any?=null,msg: Any?=null):NetworkResult<T>(null,location,msg)
    class Loading<T> : NetworkResult<T>()
}
