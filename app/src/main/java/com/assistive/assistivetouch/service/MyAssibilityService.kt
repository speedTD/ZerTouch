package com.assistive.assistivetouch.service

import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.assistive.assistivetouch.until.contrast
import com.assistive.assistivetouch.datasave.AppPreferences

class MyAssibilityService : AccessibilityService()  {

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    fun BACK_BUTTON(){
        try {
            var result =  performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        } catch (e:Exception ) {

            Log.d(contrast.TAG,e.printStackTrace().toString())
        }
    }
    fun RECENTS_BUTTON(){
        try {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
        } catch (e:Exception ) {
            Log.d(contrast.TAG,e.printStackTrace().toString())
        }
    }
    fun NOTIFICATION_BUTTON(){
        try {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS)
        } catch (e:Exception ) {
            Log.d(contrast.TAG,e.printStackTrace().toString())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
    private var isshow: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
           var action = intent?.getAction()
        if (action.equals("com.exampke.assistivetouch.Nutdanhiem"))
            RECENTS_BUTTON()
        if (action.equals("com.exampke.assistivetouch.NutQuayLai"))
            BACK_BUTTON()
        if (action.equals("com.exampke.assistivetouch.NutThongbao"))
            NOTIFICATION_BUTTON()

        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(contrast.TAG, "onDestroy Service")
        super.onDestroy()

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(contrast.TAG,"connected!")
        AppPreferences.isAccesbisilityconnected=true

    }


}