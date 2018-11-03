package com.tony.albanese.mynews.controller.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.tony.albanese.mynews.R
import com.tony.albanese.mynews.controller.fragments.TimePickerFragment
import com.tony.albanese.mynews.controller.utilities.SEARCH_ALARM_CODE
import com.tony.albanese.mynews.model.SearchAlarmReceiver
import kotlinx.android.synthetic.main.search_parameters_layout.*
import java.util.*

class NotificationActivity : AppCompatActivity(), OnTimeSetListener {

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val toast = Toast.makeText(this, "Time Set", Toast.LENGTH_SHORT)
        toast.show()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        startSearchAlarm(calendar) //This will be moved to the switch's onClick method.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_parameters_layout)

        btn_search.visibility = View.GONE
        tv_start_date.visibility = View.INVISIBLE
        tv_end_date.visibility = View.INVISIBLE
        tv_notification.visibility = View.VISIBLE
        switch_auto_search.visibility = View.VISIBLE

        tv_notification.setOnClickListener {
            val timePicker = TimePickerFragment()
            timePicker.show(supportFragmentManager, "time picker")
        }
    }


    fun startSearchAlarm(c: Calendar) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SearchAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, SEARCH_ALARM_CODE, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    fun cancelSearchAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SearchAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, SEARCH_ALARM_CODE, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}
