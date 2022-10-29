package com.app.gong4.DTO

data class ResponseStudycategoryBody(
    val location: Any,
    val msg: Any,
    val data: List<StudyCategory>
)

data class StudyCategory(
    val categoryUID : Int,
    val name : String
)