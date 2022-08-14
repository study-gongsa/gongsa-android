package com.app.gong4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenAcitivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        startActivity(Intent(this@SplashScreenAcitivity,MainActivity::class.java))
        finish()
    }
}