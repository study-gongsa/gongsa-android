package com.app.gong4.model.res

import com.app.gong4.model.StudyCategory

data class ResponseStudycategoryBody(
    val location: Any,
    val msg: Any,
    val data: List<StudyCategory>
)