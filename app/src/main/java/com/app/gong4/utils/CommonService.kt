package com.app.gong4.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.app.gong4.api.RequestServer
import com.bumptech.glide.load.model.GlideUrl
import java.text.SimpleDateFormat
import javax.inject.Inject

class CommonService {
    companion object{
        @Inject
        lateinit var tokenManager: TokenManager

        /*
        * timestamp 형태 변환 함수
        */
        fun convertTimestampToDate(time: Long): String {
            val sdf = SimpleDateFormat("yy.MM.dd")
            return sdf.format(time).toString()
        }

        /*
        * 이미지 이름 -> url
        */
        fun getImageGlide(imagePath: String): GlideUrl {
            val USER_TOKEN = tokenManager.getAccessToken()
            val IMAGE_URL = "${RequestServer.BASE_URL}/api/image/" + imagePath
            val glideUrl = GlideUrl(IMAGE_URL) { mapOf(Pair("Authorization", "Bearer $USER_TOKEN")) }
            return glideUrl
        }

        /*
        * 이미지의 URl -> 실제 기기 path
        */
        fun getRealPathFromURI(context: Context,uri : Uri):String{
            var columnIndex = 0
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context?.contentResolver?.query(uri, proj, null, null, null)
            if(cursor!!.moveToFirst()){
                columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            }
            val result = cursor.getString(columnIndex)
            cursor.close()
            return result
        }
    }
}