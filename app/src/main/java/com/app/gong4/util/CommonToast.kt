package com.app.gong4.util

import android.content.Context
import android.widget.Toast

class CommonToast(context: Context) {
    companion object{
        fun showShortToast(context:Context,text:String){
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
        }

        fun showShortLong(context:Context,text:String){
            Toast.makeText(context,text,Toast.LENGTH_LONG).show()
        }
    }
}