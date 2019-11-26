package kz.nextstep.tazalykpartners.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

open class BaseNavigationViewActivity: AppCompatActivity() {
    companion object {
        var selectedWasteId = ""
        var selectedDates = ""
        var selectedFilterType = "За месяц"
        var filterDateDays = 30

        val CHANNEL_ID = "tazalyk_partners_my_channel_id"
        val CHANNEL_NAME = "tazalyk_partners_my_channel_name"
        val CHANNEL_DESC = "tazalyk_partners_my_channel_description"
    }



    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(channel)
        }
    }


}