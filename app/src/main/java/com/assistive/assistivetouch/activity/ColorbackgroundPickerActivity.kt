package com.assistive.assistivetouch.activity

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.AppCompatImageButton
import android.widget.TextView
import android.widget.Toast
import com.assistive.assistivetouch.AmbilWarnaDialog
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.datasave.AppPreferences
import com.assistive.assistivetouch.until.Ads
import kotlinx.android.synthetic.main.activity_colorbackground_picker.*
import kotlinx.android.synthetic.main.activity_colorbackground_picker.layout_ads
import kotlin.random.Random

class ColorbackgroundPickerActivity : AppCompatActivity() {
    var Defaultcolor :Int ?=Color.parseColor("#B4030303")
    var TempColor :Int ?=null
    var checkok=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.assistive.assistivetouch.R.layout.activity_colorbackground_picker)
        var actionBar=supportActionBar
        actionBar?.displayOptions= ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.toolbarlayout)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setTitle(getResources()?.getString(R.string.title_changebackgound))
        val textviewTitle : TextView = findViewById(R.id.tvTitle)
        textviewTitle.text =getResources()?.getString( R.string.title_changebackgound)
        val toolbarbackbtn : AppCompatImageButton = this.findViewById(R.id.toolbar_back)
        toolbarbackbtn.setOnClickListener {
            finish()
        }
        val toolbarsavebtn : AppCompatImageButton = this.findViewById(R.id.toolbar_save)
        toolbarsavebtn.setOnClickListener {
            //save color
            if(checkok){
                AppPreferences.colorbackground=Defaultcolor!!
                if(Random()){
                    Ads.loadFullScreenAds(this@ColorbackgroundPickerActivity)
                }
            }
            finish()
        }
        if(AppPreferences.colorbackground!=0){

            var shapebtn :GradientDrawable =  GradientDrawable()
            shapebtn.cornerRadius=15.0f
            shapebtn.setColor(AppPreferences.colorbackground)
            btnpicker_color_backgound.background=shapebtn
            var shape :GradientDrawable =  GradientDrawable()
            shape.cornerRadius=20.0f
            shape.setColor(AppPreferences.colorbackground)
            fullTouchLayoutpicker.background=shape
        }
        btnpicker_color_backgound.setOnClickListener {
            OpenColorPickerDialog(true)
        }

        Ads.loadBannerAds(this, layout_ads)

    }
    fun Random () :Boolean{
        var ran : Random = Random
        return ran.nextBoolean()
    }
    private fun OpenColorPickerDialog(AlphaSupport: Boolean) {

        if(AppPreferences.colorbackground!=0){
            TempColor=AppPreferences.colorbackground
        }else{
            TempColor=Defaultcolor
        }

        val ambilWarnaDialog = AmbilWarnaDialog(
            this@ColorbackgroundPickerActivity,
            TempColor!!,
            AlphaSupport,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onOk(ambilWarnaDialog: AmbilWarnaDialog, colors: Int) {
                    var shapebtn :GradientDrawable =  GradientDrawable()
                    shapebtn.cornerRadius=15.0f
                     shapebtn.setColor(colors)
                    btnpicker_color_backgound.background=shapebtn
                    var shape :GradientDrawable =  GradientDrawable()
                    shape.cornerRadius=20.0f
                     shape.setColor(colors)
                    fullTouchLayoutpicker.background=shape
                    Defaultcolor = colors
                    checkok=true


                }
                override fun onCancel(ambilWarnaDialog: AmbilWarnaDialog) {
                    checkok=false
                    Toast.makeText(this@ColorbackgroundPickerActivity, getResources()?.getString(R.string.noti_colorpickerClose), Toast.LENGTH_SHORT).show()
                }
            })

        ambilWarnaDialog.show()
    }

}
