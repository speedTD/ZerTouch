package com.assistive.assistivetouch.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.util.Log
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.datasave.AppPreferences

class OnresultAcitvity2 : AppCompatActivity() {

    private val ON_DO_NOT_DISTURB_CALLBACK_CODE: Int=11

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AppPreferences.noti){
            finish()
        }
        requestMutePermissions()
        finish()
        setContentView(R.layout.activity_onresult_acitvity2)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestMutePermissions() {
        try
        {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp()

        }
        catch (e:SecurityException) {
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {
        val notificationManager = getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // if user granted access else ask for permission
            // Open Setting screen to ask for permisssion
        Log.d("thien","codeee1")
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivityForResult(intent, ON_DO_NOT_DISTURB_CALLBACK_CODE)

        }else{
            AppPreferences.noti = true
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE)
        {

            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp()
        }
    }



}
