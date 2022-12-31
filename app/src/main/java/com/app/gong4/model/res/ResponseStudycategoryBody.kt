package com.app.gong4.model.res

import com.app.gong4.model.StudyCategory

data class ResponseStudycategoryBody(
    val location: String,
    val msg: String,
    val data: List<StudyCategory>
)