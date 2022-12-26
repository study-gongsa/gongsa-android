package com.app.gong4.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.app.gong4.api.RequestServer
import com.bumptech.glide.load.model.GlideUrl
import java.text.SimpleDateFormat

class CommonService {
    companion object{
        /*
        * timestamp 형태로 바꾸는 함수
        */
        fun convertTimestampToDate(time: Long): String {
            val sdf = SimpleDateFormat("yy.MM.dd")
            val date = sdf.format(time).toString()
            return date
        }

        /*
        * 이미지 이름을 url로 바꾸는 함수
        */
        fun getImageGlide(imagePath: String): GlideUrl {
            val USER_TOKEN = MainApplication.prefs.getData("accessToken", "")
            val IMAGE_URL = "${RequestServer.BASE_URL}/api/image/" + imagePath
            val glideUrl = GlideUrl(IMAGE_URL) { mapOf(Pair("Authorization", "Bearer $USER_TOKEN")) }
            return glideUrl
        }

        /*
        * 이미지의 URI를 실제 path로 바꾸는 함수
        */
        fun getRealPathFromURI(context: Context,uri : Uri):String{
            val buildName = Build.MANUFACTURER

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