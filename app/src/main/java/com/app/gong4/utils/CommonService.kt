package com.app.gong4.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.app.gong4.MainApplication
import com.app.gong4.utils.Constants.BASE_URL
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.text.SimpleDateFormat

class CommonService {
    companion object{

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
            val USER_ACCESS_TOKEN = MainApplication.tokenManager.getAccessToken()
            val IMAGE_URL = "${BASE_URL}/api/image/" + imagePath
            val glideUrl = GlideUrl(IMAGE_URL,
                LazyHeaders.Builder().addHeader("Authorization","Bearer $USER_ACCESS_TOKEN").build())
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