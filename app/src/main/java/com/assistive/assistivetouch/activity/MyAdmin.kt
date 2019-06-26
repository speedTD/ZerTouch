package com.assistive.assistivetouch.activity

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.assistive.assistivetouch.until.contrast

class MyAdmin : DeviceAdminReceiver() {
    override fun onEnabled(context: Context?, intent: Intent?) {
        Log.d(contrast.TAG," bật quyền quản trị ")
        super.onEnabled(context, intent)
    }

    override fun onDisabled(context: Context?, intent: Intent?) {
        Log.d(contrast.TAG," Tắt quyền quản trị ")
        super.onDisabled(context, intent)
    }
}