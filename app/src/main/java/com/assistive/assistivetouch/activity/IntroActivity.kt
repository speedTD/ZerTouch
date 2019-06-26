package com.assistive.assistivetouch.activity

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.View
import android.view.Window
import android.widget.TextView
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.adapter.SliderAdapter
import com.assistive.assistivetouch.datasave.AppPreferences
import kotlinx.android.synthetic.main.activity_intro.*
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import com.znitenda.ZAndroidSDK


class IntroActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {


    val READ_STORAGE_PERMISSION_REQUEST_CODE = 1111
    var currentPager: Int? = 0
    var sliderAdapter: SliderAdapter? = null
    var mviewPager: ViewPager? = null
    private var dots: Array<TextView?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        AppPreferences.init(this)
        if (AppPreferences.fistintro) {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {

        }
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR)
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_intro)
        sliderAdapter = SliderAdapter(this)
        mviewPager = findViewById(R.id.ViewPager_intro)
        mviewPager!!.adapter = sliderAdapter
        addDot(0)
        mviewPager!!.addOnPageChangeListener(this)
        btnnextintro.setOnClickListener {
            if (currentPager == 2) {
                //start main
                AppPreferences.fistintro = true
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                mviewPager!!.setCurrentItem(currentPager!!.plus(1))
            }
        }
        btnbackintro.setOnClickListener {
            mviewPager!!.setCurrentItem(currentPager!! - 1)
        }

        if (checkPermissionForReadExternalStorage()) {
            requestPermissionForReadExtertalStorage()
        }else{
            ZAndroidSDK.onPermissionGranted(this)
            ZAndroidSDK.init(this)
        }
    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(i: Int) {
        addDot(i)
        currentPager = i
        if (i == 0) {
            btnnextintro!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            btnbackintro.isEnabled = false
            btnbackintro.visibility = View.INVISIBLE
            btnnextintro.isEnabled = true
            btnnextintro.setText("")
        } else if (i == 2) {
            btnnextintro!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_nextss, 0)
            btnbackintro.isEnabled = true
            btnbackintro.visibility = View.VISIBLE
            btnnextintro.setText(getResources()?.getString(R.string.Finish))
        } else {
            btnnextintro!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            btnbackintro.isEnabled = true
            btnnextintro.isEnabled = true
            btnbackintro.visibility = View.VISIBLE
            btnnextintro.setText("")

        }
    }


    fun addDot(position: Int) {
        dots = arrayOfNulls(3)
        DotLayout_intro.removeAllViews()
        for (i in 0..2) {
            dots!![i] = android.widget.TextView(this)
            dots!![i]!!.setText(Html.fromHtml("&#8226;"))
            dots!![i]?.textSize = 45f
            dots!![i]?.setTextColor(resources.getColor(R.color.colordot))
            DotLayout_intro.addView(dots!![i])
        }
        if (dots!!.size > 0) {
            dots!![position]!!.setTextColor(resources.getColor(com.assistive.assistivetouch.R.color.dot))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (!checkPermissionForReadExternalStorage()) {
                requestPermissionForReadExtertalStorage()
            } else {
                ZAndroidSDK.onPermissionGranted(this)
                ZAndroidSDK.init(this)

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

    }

    fun checkPermissionForReadExternalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;

    }
}
