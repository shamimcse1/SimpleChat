package net.beingup.simplechat.HelperClasses

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.widget.RemoteViews
import net.beingup.simplechat.Activities.StartPage
import net.beingup.simplechat.R

// Download From : https://nulledsourcecode.com/
//Contact us For Reskin And Making Money Earning Apps

class SystemMethods(private val mContext: Context) {

    /**
     * This function use for send message notification.
     */
    fun notification(title: String, description: String){
        val intent = Intent(mContext, StartPage::class.java)
        val pendingIntent = PendingIntent.getActivity(mContext,0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(mContext.packageName, R.layout.notification_layout)
        contentView.setTextViewText(R.id.noti_title, title)
        contentView.setTextViewText(R.id.noti_content, description)

        val notificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: Notification.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(mContext.getString(R.string.app_name) ,description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(mContext, mContext.getString(R.string.app_name))
        } else {
            builder = Notification.Builder(mContext)
        }

        builder.setContent(contentView)
            .setSmallIcon(R.drawable.app_icon)
            .setLargeIcon(BitmapFactory.decodeResource(mContext.resources, R.drawable.app_icon))
            .setContentIntent(pendingIntent)

        notificationManager.notify(123, builder.build())
    }

    /**
     * Play notification tune.
     */
    fun notificationTune(){
        val media = MediaPlayer.create(mContext, R.raw.noti)
        media.start()
    }
}