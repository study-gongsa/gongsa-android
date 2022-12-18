package com.app.gong4.util

import java.text.SimpleDateFormat

class CommonService {
    fun convertTimestampToDate(time: Long): String {
        val sdf = SimpleDateFormat("yy.MM.dd")
        val date = sdf.format(time).toString()
        return date
    }
}