package com.app.gong4.util

import com.app.gong4.api.RequestServer
import com.bumptech.glide.load.model.GlideUrl
import java.text.SimpleDateFormat

class CommonService {
    fun convertTimestampToDate(time: Long): String {
        val sdf = SimpleDateFormat("yy.MM.dd")
        val date = sdf.format(time).toString()
        return date
    }

    fun getImageGlide(imagePath: String): GlideUrl {
        val USER_TOKEN = MainApplication.prefs.getData("accessToken", "")
        val IMAGE_URL = "${RequestServer.BASE_URL}/api/image/" + imagePath
        val glideUrl = GlideUrl(IMAGE_URL) { mapOf(Pair("Authorization", "Bearer $USER_TOKEN")) }
        return glideUrl
    }
}