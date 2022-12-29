package com.app.gong4.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.gong4.MainActivity
import com.app.gong4.R
import com.app.gong4.api.RequestServer
import com.app.gong4.model.BaseResponse
import com.app.gong4.model.RequestDeviceToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MessagingService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showNotification(remoteMessage.notification!!)
    }

    private fun sendTokenToServer(token: String){
        val deviceBody = RequestDeviceToken(token)
        RequestServer.userService.saveUserDeviceToken(deviceBody).enqueue(object : Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("token-",response.body().toString())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.my_notification_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setContentIntent(pIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSystemService(NotificationManager::class.java).run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel =
                        NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                    createNotificationChannel(channel)
                }

                notify(Date().time.toInt(), notificationBuilder.build())
            }
        }
    }
}

