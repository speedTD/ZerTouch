package com.assistive.assistivetouch.activity

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import kotlinx.android.synthetic.main.activity_main.*

import com.assistive.assistivetouch.Interface.ChangeTouchlistener
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.model.Eventmodel
import com.assistive.assistivetouch.model.Appmodel
import com.assistive.assistivetouch.customdialog.dialogpickericon
import com.assistive.assistivetouch.service.MyTouchService

import android.app.ActivityManager
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.*

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ShareCompat
import android.util.Base64
import com.assistive.assistivetouch.Interface.ChangeAdminPermision
import com.assistive.assistivetouch.Interface.changetouchdemolistener
import com.assistive.assistivetouch.datasave.AppPreferences
import com.assistive.assistivetouch.datasave.Database
import com.assistive.assistivetouch.customdialog.dialogpermision
import com.assistive.assistivetouch.until.*
import kotlin.random.Random


class MainActivity : AppCompatActivity(), ChangeTouchlistener,
    changetouchdemolistener,ChangeAdminPermision {
    override fun ChangerAdmin(boolean: Boolean) {
        main_adminstrator.isChecked=boolean
    }
    override fun OnIconChangerdemo(bitmap: Bitmap) {
        //get csdl sau do set
        MyTouchService.btntouch?.setImageBitmap(bitmap)
    }

    var devicePolicyManager: DevicePolicyManager?=null
    var com: ComponentName?=null
    val CODE_PAINT_TOUCH=7878
    var intentstart:Intent ?=null

    override fun OnIconChanger(bitmap: Bitmap) {
        MyTouchService.btntouch?.setImageBitmap(bitmap)
        MyTouchService.btntouch?.setImageBitmap(decodeBase64(AppPreferences.icon))
        main_touchvew.setImageBitmap(decodeBase64(AppPreferences.icon))
        if(Random()){
            Ads.loadFullScreenAds(this)
        }
    }
    fun Random () :Boolean{
        var ran : Random = Random
        return ran.nextBoolean()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.init(this)
        if(AppPreferences.firstRun==false){
            AppPreferences.icon=""
            AppPreferences.iconnumber=1
            AppPreferences.colorbackground=0
            AppPreferences.isadmin=false
            AppPreferences.firstRun=true
            //tạo mới csdl và thêm vào 8 hàng tương ứng vói 8 nút
            databaseFistInsert()
            //wifi, bluetooch,denpin,tu xoay
        }else{

        }
        setContentView(R.layout.activity_main)
        /*
         */
        GetColorIcon()
        verifyStoragePermissions()
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, CODE_PAINT_TOUCH)
            } else {

            }
        }
        if (isMyServiceRunning(MyTouchService::class.java)) {
            swich_on_off_touch.isChecked = true

        } else {
            swich_on_off_touch.isChecked = false
        }
        val face = Typeface.createFromAsset(
           assets,
            "fonts/SanFranciscoDisplay_Regular_1.otf"
        )
        val faceappname = Typeface.createFromAsset(
            assets,
            "fonts/SanFranciscoDisplay_Bold_1.otf"
        )
        swich_on_off_touch.setTypeface(face)
        main_adminstrator.setTypeface(face)
        main_changecolor.setTypeface(face)
        main_layout.setTypeface(face)
        main_share.setTypeface(face)
        main_changeIconTouch.setTypeface(face)
        main_security.setTypeface(face)
        main_rateapp.setTypeface(face)
        main_appname.setTypeface(faceappname)
            intentstart = Intent(this, MyTouchService::class.java)
            swich_on_off_touch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (!Settings.canDrawOverlays(this)) {
                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:$packageName")
                            )
                            startActivityForResult(intent, CODE_PAINT_TOUCH)
                        } else {
                            startService(intentstart)
                        }
                    } else {
                        startService(intentstart)
                    }
                } else {
                    if(Random()){
                        Ads.loadFullScreenAds(this)
                    }
                    stopService(intentstart)
                }
            }

        if(AppPreferences.isadmin){
            main_adminstrator.setTextColor(Color.BLACK)
            main_adminstrator.isChecked=true
            main_adminstrator.isEnabled=true
        }else{
            main_adminstrator.setTextColor(Color.parseColor("#FF535353"))
            main_adminstrator.isChecked=false
            main_adminstrator.isEnabled=true
        }
        main_adminstrator.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                showdialog()
            }else{
                devicePolicyManager = getSystemService(Service.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                com = ComponentName(this, MyAdmin::class.java)
                devicePolicyManager!!.removeActiveAdmin(com)
                AppPreferences.isadmin=false
                main_adminstrator.setTextColor(Color.parseColor("#FF535353"))
                main_adminstrator.isChecked=false
                main_adminstrator.isEnabled=true
            }
        }

        main_changecolor.setOnClickListener {
            val i2: Intent = Intent(applicationContext, ColorbackgroundPickerActivity::class.java)
            startActivity(i2)
        }
        main_changeIconTouch.setOnClickListener {
            var dialogpickericon: dialogpickericon = dialogpickericon(this,this,this)
            dialogpickericon.show()
        }

        main_layout.setOnClickListener {
            val i2: Intent = Intent(applicationContext, LayoutTouchChangeActivity::class.java)
            startActivity(i2)
        }
        main_security.setOnClickListener {
            val i2: Intent = Intent(applicationContext, SecurityAboutActivity::class.java)
            startActivity(i2)
        }
        main_rateapp.setOnClickListener {
            val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)))
            }
        }
        main_share.setOnClickListener {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle("Share")
                .setText("http://play.google.com/store/apps/details?id=" + this.getPackageName())
                .startChooser()
        }

        Ads.loadBannerAds(this, layout_ads)

    }

    override fun onResume() {
        GetColorIcon()
        super.onResume()
    }

    private fun GetColorIcon() {
        //Kiểm tra color đã có dl chưa
        //icon đã có dl chưa
        if(AppPreferences.colorbackground!=0){
            var shape : GradientDrawable =  GradientDrawable()
            shape.cornerRadius=10.0f
            shape.setColor(AppPreferences.colorbackground)
            main_colorview.background=shape
        }
        if(!AppPreferences.icon.equals("")){
            if(!AppPreferences.icon.equals("STRING_TOO_LARGE")){
                main_touchvew.setImageBitmap(decodeBase64(AppPreferences.icon))
            }else{

            }
        }
    }
    fun decodeBase64(input: String): Bitmap {
        val decodedByte = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>( Manifest.permission.CAMERA)

    fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
    fun databaseFistInsert(){
        val data:Database=Database(this)
        //thêm mặc định appname
        data.insertApp(Appmodel("null","null"))
        data.insertApp(Appmodel("null","null"))
        data.insertApp(Appmodel("null","null"))
        data.insertApp(Appmodel("null","null"))
        data.insertApp(Appmodel("null","null"))
        data.insertApp(Appmodel("null","null"))
        data.insertApp(Appmodel("null","null"))
        data.insertApp(Appmodel("null","null"))
        data.insertEvent(Eventmodel(1,0,""))
        data.insertEvent(Eventmodel(2,0,""))
        data.insertEvent(Eventmodel(3,0,""))
        data.insertEvent(Eventmodel(4,0,""))
        data.insertEvent(Eventmodel(5,0,""))
        data.insertEvent(Eventmodel(6,0,""))
        data.insertEvent(Eventmodel(7,0,""))
        data.insertEvent(Eventmodel(8,0,""))
        data.insertEventFake(Eventmodel(1,0,""))
        data.insertEventFake(Eventmodel(2,0,""))
        data.insertEventFake(Eventmodel(3,0,""))
        data.insertEventFake(Eventmodel(4,0,""))
        data.insertEventFake(Eventmodel(5,0,""))
        data.insertEventFake(Eventmodel(6,0,""))
        data.insertEventFake(Eventmodel(7,0,""))
        data.insertEventFake(Eventmodel(8,0,""))
    }
    fun showdialog(){
      val dialogpermision= dialogpermision(this,this)
        dialogpermision.show()
    }
}


