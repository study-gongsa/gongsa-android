package com.app.gong4.model

data class ResponseMyStudyGroupRankingBody(
    val location: String,
    val msg: String,
    val data: StudyGroup
)

data class StudyGroup(
    val groupRankList: List<Ranking>
)

data class Ranking(
    val groupUID: Int,
    val members: List<Member>,
    val name: String
)