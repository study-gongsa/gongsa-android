package com.app.gong4

data class ResponseUserCategory(
    val location: Any,
    val msg: Any,
    val data: List<UserCategory>,
)

data class UserCategory(
    val categoryUID: Int,
    val userCategoryUID: Int,
    val userUID: Int
)
data class RequestSaveUserCateogry(
    var categoryUIDs:List<Int>
)
data class ResponseSaveUserCategory(
    val location: Any,
    val msg: Any,
    val data:Any
)