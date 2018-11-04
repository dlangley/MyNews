package com.tony.albanese.mynews.model

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.tony.albanese.mynews.R
import com.tony.albanese.mynews.controller.activities.MainActivity
import com.tony.albanese.mynews.controller.utilities.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.HttpURLConnection

class SearchAlarmReceiver : BroadcastReceiver() {

    var list = ArrayList<Article>()

    override fun onReceive(context: Context?, intent: Intent?) {
        val url = intent?.getStringExtra("notification_url")
        if (url != null && url.isNotEmpty() && url.isNotBlank()) {
            getNewArticles(context!!, url)
        }

    }

    fun sendNotification(context: Context) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(TAB, CUSTOM_SEARCH_TAB)
        val notificationPendingIntent = PendingIntent.getActivity(context, SEARCH_ALARM_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_stat_new_article)
                .setContentTitle("My News App Messahr")
                .setContentText("You have new articles.")
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        notificationManager.notify(SEARCH_ALARM_CODE, builder.build());
    }

    fun getNewArticles(c: Context, url: String) {
        var connection: HttpURLConnection?
        var result = ""
        if (networkIsAvailable(c)) {
            connection = connectToSite(stringToUrl(url)!!)

            doAsync {
                if (connection != null) {
                    result = readDataFromConnection(connection)
                }
                uiThread {
                    val tempList = generateArticleArray(CUSTOM_SEARCH_RESULTS, result)
                    list = tempList
                    if (list.isNotEmpty()) {
                        val prefs = c.applicationContext.getSharedPreferences(NOTIFICATION_PREFERENCES, MODE_PRIVATE)
                        saveArrayListToSharedPreferences(prefs, NEW_ARTICLE_KEY, list)
                        sendNotification(c!!)
                    }
                }
            }
        }

    }
}