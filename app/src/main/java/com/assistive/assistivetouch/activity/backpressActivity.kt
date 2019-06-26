package com.assistive.assistivetouch.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.assistive.assistivetouch.until.contrast
import com.assistive.assistivetouch.R

class backpressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backpress)
        this.onBackPressed()
        KeyEvent(KeyEvent.KEYCODE_BACK,KeyEvent.ACTION_DOWN)
//        this.dispatchKeyEvent(KeyEvent(GLOBAL_ACTION_BACK,KeyEvent.KEYCODE_BACK))
        Log.d(contrast.TAG,"backpress")
        finish()
    }
}
