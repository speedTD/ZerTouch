package com.assistive.assistivetouch.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.assistive.assistivetouch.R
import android.graphics.Typeface
import android.support.v7.app.ActionBar
import android.support.v7.widget.AppCompatImageButton
import android.text.Html
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_security_about.*


class SecurityAboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_about)
        val actionBar=supportActionBar
        actionBar?.displayOptions= ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.toolbarlayout)
        actionBar?.setDisplayShowHomeEnabled(true)
        val textviewTitle : TextView = findViewById(R.id.tvTitle)
        textviewTitle.text =getResources()?.getString( R.string.titleActionbar_Security)
        val toolbarbackbtn : AppCompatImageButton = this.findViewById(R.id.toolbar_back)
        toolbarbackbtn.setOnClickListener {
            finish()
        }
        val toolbarsavebtn : AppCompatImageButton = this.findViewById(R.id.toolbar_save)
        toolbarsavebtn.setOnClickListener {

            finish()
        }
        val face = Typeface.createFromAsset(
            assets,
            "fonts/SanFranciscoDisplay_Regular_1.otf"
        )
        tv_contentSecurity.setTypeface(face)
        tv_contentSecurity.setText(Html.fromHtml(getString(R.string.desc)))
    }
}
