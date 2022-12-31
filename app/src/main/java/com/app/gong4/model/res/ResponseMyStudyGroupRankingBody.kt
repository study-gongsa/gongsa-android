package com.app.gong4.model

data class ResponseMyStudyGroupRankingBody(
    val location: String,
    val msg: String,
    val data: StudyGroup
){
    data class StudyGroup(
        val groupRankList: List<Ranking>
    )
}