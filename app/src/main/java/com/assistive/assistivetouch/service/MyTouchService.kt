package com.assistive.assistivetouch.service

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.app.admin.DevicePolicyManager
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.hardware.Camera
import android.hardware.camera2.*
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Vibrator
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.assistive.assistivetouch.until.contrast
import com.assistive.assistivetouch.activity.OnresultAcitvity2
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.activity.MainActivity
import com.assistive.assistivetouch.activity.MyAdmin
import com.assistive.assistivetouch.activity.OnResultAcitvity
import com.assistive.assistivetouch.activity.PakageAppActivity

import com.assistive.assistivetouch.datasave.AppPreferences
import com.assistive.assistivetouch.datasave.Database


import java.io.ByteArrayOutputStream
import java.util.*

class MyTouchService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mPreviewSession: CameraCaptureSession? = null
    private var mPreviewBuilder: CaptureRequest.Builder? = null
    var instance: MyAssibilityService? = null
    val CHANNEL_ID = "my_channel_01"

    var mwindowManager: WindowManager? = null;
    var mFloatingView: View? = null
    var mIsFloatingViewAttached: Boolean? = null
    var fullLayout: ViewGroup? = null

    companion object {
        fun getInstance(): MyAssibilityService {
            return myAccessibilityServiceInstance!!
        }

        var myAccessibilityServiceInstance: MyAssibilityService? = null
        var btntouch: ImageView? = null
        var db: Database? = null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
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

        /*
        test view
         */

        //layout full
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                ""
            }
        db = Database(this)
        var notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var notiBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_panelhome)
            .setAutoCancel(true)
            .setContentTitle("Assistive touch ")
            .setContentText(getResources()?.getString(R.string.warning_Returnapp))
            .build()

        notiBuilder.contentIntent = pendingIntent
        notiManager.notify(1, notiBuilder)
        startForeground(1, notiBuilder)
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.head, null)

        val LAYOUT_FLAG: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
        }
        mwindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = 0
        layoutParams.y = 0
        //trước khi addview set icon cho touch
        btntouch = mFloatingView!!.findViewById(R.id.icontouch)
        setIconTouch()
        mwindowManager!!.addView(mFloatingView, layoutParams)
        mIsFloatingViewAttached = true
        val display = mwindowManager!!.defaultDisplay
        val screenWith = display.width
        val screenHeight = display.height

        //check event touch Imageview
        mFloatingView?.setOnTouchListener(object : View.OnTouchListener {
            var startime: Long? = null
            var initX: Int? = null
            var initY: Int? = null
            var initTouchX: Float? = null
            var initTouchY: Float? = null
            var islongclick: Boolean? = null
            val timeTouchLongClick = 5000
            var isclick = false;
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {
                        islongclick = true
                        initTouchX = event.rawX
                        initTouchY = event.rawY
                        initX = layoutParams.x
                        initY = layoutParams.y
                        //set startime
                        startime = System.currentTimeMillis();


                    }
                    MotionEvent.ACTION_MOVE -> {

                        layoutParams.x = initX!! + (event.getRawX() - this.initTouchX!!).toInt()
                        layoutParams.y = initY!! + (event.getRawY() - this.initTouchY!!).toInt()
                        if (!isshow) {
                            mwindowManager?.updateViewLayout(mFloatingView, layoutParams)
                            val handler = Handler()
                            val t = Timer()
                            t.schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(object : Runnable {
                                        public override fun run() {
                                            //DO SOME ACTIONS HERE , THIS ACTIONS WILL WILL EXECUTE AFTER 5 SECONDS...
                                            setMouseColor(true)
                                        }
                                    })
                                }
                            }, 5000)
                        } else {

                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        setMouseColor(false)
                        islongclick = false
                        val Xdiff = (event.rawX - initTouchX?.toInt()!!)
                        val Ydiff = (event.rawY - initTouchY?.toInt()!!)

                        if (!isshow) {
                            if (layoutParams.x < screenWith / 2) {
                                while (layoutParams.x > 0) {
                                    layoutParams.x -= 10
                                    mwindowManager!!.updateViewLayout(mFloatingView, layoutParams)
                                }
                            } else {
                                while (layoutParams.x < screenWith) {
                                    layoutParams.x += 10
                                    mwindowManager!!.updateViewLayout(mFloatingView, layoutParams)
                                }
                            }
                        }
                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        var timeDelay: Long = System.currentTimeMillis() - startime!!
                        if (Xdiff < 10 && Ydiff < 10 && timeDelay < 1000) {
                            Log.d(contrast.TAG, "one click time")
                            isclick = !isclick
                            if (isclick) {
                                Showlayoutmain()
                            }

                        }
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            if (islongclick!!) {
                                if (timeDelay > 1000) {
                                    val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                    vibrator.vibrate(100)
                                    // HideTouch3D()
                                }
                            }
                        }, timeTouchLongClick.toLong())

                        return true

                    }
                }
                return false
            }

        })
    }


    val DELAY_MILLIS = 5000
    var mHandler: Handler? = null
    private fun setMouseColor(touch: Boolean) {
        if (touch) {
            val alpha = AlphaAnimation(0.5f, 0.5f) // change values as you want
            alpha.setDuration(0) // Make animation instant
            alpha.setFillAfter(true) // Tell it to persist after the animation ends
            btntouch?.startAnimation(alpha)
            Log.d(contrast.TAG, "set alpha")
            mFloatingView?.background?.alpha = 40
            mHandler?.postDelayed(mRunnable, DELAY_MILLIS.toLong())
        } else {
            Log.d(contrast.TAG, "remove alpha")
            val alpha = AlphaAnimation(1f, 1f) // change values as you want
            alpha.setDuration(0) // Make animation instant
            alpha.setFillAfter(true) // Tell it to persist after the animation ends
            btntouch?.startAnimation(alpha)
            mFloatingView?.background?.alpha = 1
            mHandler?.postDelayed(mRunnable, DELAY_MILLIS.toLong())
        }
    }

    private val mRunnable = object : Runnable {
        public override fun run() {
            setMouseColor(true)
        }
    }


    var mwindownmain: WindowManager? = null;
    var mviewmain: View? = null
    var tablelayout: ViewGroup? = null
    private fun HideLayoutmain() {
        mviewmain?.visibility = View.GONE
        mFloatingView?.visibility = View.VISIBLE
    }

    var btnhome: TextView? = null
    var btnControl: TextView? = null
    var btnapp: TextView? = null
    var btnlock: TextView? = null
    var btnRescent: TextView? = null
    var btnringmode: TextView? = null
    var btnshowNotification: TextView? = null
    var btnbackpress: TextView? = null

    var btnhomeImg: ImageView? = null
    var btnControlImg: ImageView? = null
    var btnappImg: ImageView? = null
    var btnlockImg: ImageView? = null
    var btnRescentImg: ImageView? = null
    var btnringmodeImg: ImageView? = null
    var btnshowNotificationImg: ImageView? = null
    var btnbackpressImg: ImageView? = null
    private fun Showlayoutmain() {

        val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // ẩn floatbutton
        // hiện layout main dang full Screen
        mFloatingView?.visibility = View.GONE
        mviewmain = LayoutInflater.from(this).inflate(R.layout.maintouch, null)
        fullLayout = mviewmain!!.findViewById(R.id.maintouch_fullTouchLayout)
        tablelayout = mviewmain!!.findViewById(R.id.maintouch_layouttable)
        btnhome = mviewmain!!.findViewById(R.id.control_manhinhchinh)
        btnControl = mviewmain!!.findViewById(R.id.control_dieukhien)
        btnapp = mviewmain!!.findViewById(R.id.control_ungdung)
        btnlock = mviewmain!!.findViewById(R.id.control_khoamanhinh)
        btnRescent = mviewmain!!.findViewById(R.id.control_danhiem)
        btnringmode = mviewmain!!.findViewById(R.id.control_chedoam)
        btnshowNotification = mviewmain!!.findViewById(R.id.control_thongbao)
        btnbackpress = mviewmain!!.findViewById(R.id.control_quaylai)

        btnhomeImg = mviewmain!!.findViewById(R.id.control_manhinhchinh_img)
        btnControlImg = mviewmain!!.findViewById(R.id.control_dieukhien_img)
        btnappImg = mviewmain!!.findViewById(R.id.control_ungdung_img)
        btnlockImg = mviewmain!!.findViewById(R.id.control_khoamanhinh_img)
        btnRescentImg = mviewmain!!.findViewById(R.id.control_danhiem_img)
        btnringmodeImg = mviewmain!!.findViewById(R.id.control_chedoam_img)
        btnshowNotificationImg = mviewmain!!.findViewById(R.id.control_thongbao_img)
        btnbackpressImg = mviewmain!!.findViewById(R.id.control_quaylai_img)


        val eventmodel1 = db?.getbyID(1)
        if (eventmodel1?.iconevent != 0) {
            CheckIconText(1)
        }
        val eventmodel2 = db?.getbyID(2)
        if (eventmodel2?.iconevent != 0) {
            CheckIconText(2)
        }
        val eventmodel3 = db?.getbyID(3)
        if (eventmodel3?.iconevent != 0) {
            CheckIconText(3)
        }
        val eventmodel4 = db?.getbyID(4)
        if (eventmodel4?.iconevent != 0) {
            CheckIconText(4)
        }
        val eventmodel5 = db?.getbyID(5)
        if (eventmodel5?.iconevent != 0) {
            CheckIconText(5)
        }
        val eventmodel6 = db?.getbyID(6)
        if (eventmodel6?.iconevent != 0) {
            CheckIconText(6)
        }
        val eventmodel7 = db?.getbyID(7)
        if (eventmodel7?.iconevent != 0) {
            CheckIconText(7)
        }

        val eventmodel8 = db?.getbyID(8)
        if (eventmodel8?.iconevent != 0) {

            CheckIconText(8)
        }
        //event all app
        btnappImg!!.setOnClickListener {
            SetupAnim(btnapp!!)
            val eventmodel = db?.getbyID(1)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                showlayoutApp()
            }
        }
        btnbackpressImg!!.setOnClickListener {
            SetupAnim(btnbackpress!!)
            val eventmodel = db?.getbyID(2)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                NutQuaylai()
            }
        }
        btnRescentImg!!.setOnClickListener {
            SetupAnim(btnRescent!!)
            val eventmodel = db?.getbyID(3)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                NutDanhiem()
            }

        }
        //event go home
        btnhomeImg!!.setOnClickListener {
            SetupAnim(btnhome!!)
            val eventmodel = db?.getbyID(4)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                NutVeTrangchu()
            }

        }

        btnshowNotificationImg!!.setOnClickListener {
            SetupAnim(btnshowNotification!!)
            val eventmodel = db?.getbyID(5)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                NutHienThiThongbao()
            }
        }
        val eventmodels = db?.getbyID(6)
        if(eventmodels?.idevent==6) {
            val audioManagers: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (audioManagers.ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                btnringmode!!.setText(getResources()?.getString(R.string.panel_main_ringmodeon))
            } else {
                btnringmode!!.setText(getResources()?.getString(R.string.panel_main_ringmodeoff))
            }
        }
        btnringmodeImg!!.setOnClickListener {
            SetupAnim(btnringmode!!)
            val eventmodel = db?.getbyID(6)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                NutChedoam()
            }

        }
        //event control
        btnControlImg!!.setOnClickListener {
            SetupAnim(btnControl!!)
            val eventmodel = db?.getbyID(7)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                Nutdieukhien()
            }

        }
        //event lockscreen
        btnlockImg!!.setOnClickListener {
            SetupAnim(btnlock!!)
            val eventmodel = db?.getbyID(8)
            if (eventmodel?.iconevent != 0) {
                CheckEvent(eventmodel?.idevent!!)
            } else {
                NutKhoaManHinh()
            }

        }

        val LAYOUT_FLAG: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
        }
        mwindownmain = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.CENTER
        layoutParams.x = 0
        layoutParams.y = 0
        setbackgoundisSave()
        mwindownmain!!.addView(mviewmain, layoutParams)
        //bắt 1 số sự kiện trên lay out
        mviewmain?.setOnTouchListener(object : View.OnTouchListener {

            var startime: Long? = null
            var initX: Int? = null
            var initY: Int? = null
            var initTouchX: Float? = null
            var initTouchY: Float? = null
            var islongclick: Boolean? = null
            val timeTouchLongClick = 5000
            var isclick = false;

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        islongclick = true
                        Log.d(contrast.TAG, "assitive Touch down")
                        initTouchX = event.rawX
                        initTouchY = event.rawY
                        initX = layoutParams.x
                        initY = layoutParams.y
                        //set startime
                        startime = System.currentTimeMillis();

                    }
                    MotionEvent.ACTION_MOVE -> {

                    }
                    MotionEvent.ACTION_UP -> {
                        islongclick = false
                        val Xdiff = (event.rawX - initTouchX?.toInt()!!)
                        val Ydiff = (event.rawY - initTouchY?.toInt()!!)
                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        var timeDelay: Long = System.currentTimeMillis() - startime!!
                        if (Xdiff < 10 && Ydiff < 10 && timeDelay < 1000) {
                            Log.d(contrast.TAG, "one click time")
                            isclick = !isclick
                            if (isclick) {
                                // quay trở lại floatbutton
                                HideLayoutmain()
                            } else {

                            }
                        }
                        return true
                    }
                }
                return false
            }

        })

    }

    fun SetupAnim(v: TextView) {

        val anim = ScaleAnimation(
            1.5f, 1f, // Start and end values for the X axis scaling
            1.5f, 1f, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 1f
        ) // Pivot point of Y scaling
        anim.setFillAfter(true) // Needed to keep the result of the animation
        anim.setDuration(300)
        v.startAnimation(anim)
    }

    private fun setIconTouch() {
        if (AppPreferences.icon.equals("")) {
            Log.d(contrast.TAG, "null icon")
        } else {
            Log.d(contrast.TAG, "have icon")
            var bitmap = decodeBase64(AppPreferences.icon)
            btntouch?.setImageBitmap(bitmap)

        }
    }

    fun encodeTobase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()

        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun decodeBase64(input: String): Bitmap {
        val decodedByte = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }
    var create=false
    var misDialogviewAttached = false
    var mwindowManagerdialog: WindowManager? = null;
    var mViewdialog: View? = null
    var mViewControl: ViewGroup? = null
    var mflashLight: TextView? = null
    var mWifi: TextView? = null
    var mPlane: TextView? = null
    var mLight: TextView? = null
    var mRoteScreen: TextView? = null
    var mbluetooth: TextView? = null

    var mflashLightImg: ImageView? = null
    var mWifiImg: ImageView? = null
    var mPlaneImg: ImageView? = null
    var mLightImg: ImageView? = null
    var mRoteScreenImg: ImageView? = null
    var mbluetoothImg: ImageView? = null
    var mbackmain: ImageView? = null

    private fun ShowdialogControl() {
        mFloatingView?.visibility = View.GONE
        btntouch?.visibility = View.GONE
        fullLayout?.visibility = View.GONE
        mViewdialog = LayoutInflater.from(this).inflate(R.layout.dialogtouchlayoutcontrol, null);
        val LAYOUT_FLAG: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
        }
        mwindowManagerdialog = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.CENTER
        layoutParams.x = 0
        layoutParams.y = 0
        mViewControl = mViewdialog!!.findViewById(R.id.dialog_layouttable)
        setbackgoundControl()

        mwindowManagerdialog!!.addView(mViewdialog, layoutParams)
        //bắt 1 số sự kiện trên lay out
        mflashLight = mViewdialog!!.findViewById(R.id.control_denpin)
        mWifi = mViewdialog!!.findViewById(R.id.control_wifi)
        mPlane = mViewdialog!!.findViewById(R.id.control_maybay)
        mLight = mViewdialog!!.findViewById(R.id.control_chedosang)
        mRoteScreen = mViewdialog!!.findViewById(R.id.control_tuxoay)
        mbluetooth = mViewdialog!!.findViewById(R.id.control_bluetooth)
        mbackmain = mViewdialog!!.findViewById(R.id.control_backmain)

        mflashLightImg = mViewdialog!!.findViewById(R.id.control_denpin_img)
        mWifiImg  = mViewdialog!!.findViewById(R.id.control_wifi_img)
        mPlaneImg  = mViewdialog!!.findViewById(R.id.control_maybay_img)
        mLightImg  = mViewdialog!!.findViewById(R.id.control_chedosang_img)
        mRoteScreenImg  = mViewdialog!!.findViewById(R.id.control_tuxoay_img)
        mbluetoothImg  = mViewdialog!!.findViewById(R.id.control_bluetooth_img)


        val filter: IntentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(mReceiver, filter)
        IsRotate()
        mRoteScreenImg!!.setOnClickListener {
            NutxoayTudong()
        }
        var value = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS)
        if (value > 140) {
            mLight!!.setTextColor(resources.getColor(R.color.colorOn))
            mLightImg!!.setImageResource(R.drawable.ic_panellight_on)

        } else {
            mLight!!.setTextColor(resources.getColor(R.color.colorOff))
            mLightImg!!.setImageResource( R.drawable.ic_panellight_off)
        }
        mLightImg!!.setOnClickListener {
            NutChedoSang()
        }

        var checkstatebluetooth = false
        IsBlueTouch()
        mbluetoothImg!!.setOnClickListener {
            checkstatebluetooth = !checkstatebluetooth
            if (checkstatebluetooth) {
                setBluetooth(true)
            } else {
                setBluetooth(false)
            }
        }
        //event back
        mbackmain!!.setOnClickListener {
            mViewdialog?.visibility = View.GONE
            btntouch?.visibility = View.VISIBLE
            fullLayout?.visibility = View.VISIBLE
        }
        //event on off flash
           if (android.os.Build.VERSION.SDK_INT >= 27) {
               if (create) {
                   mflashLight!!.setTextColor(resources.getColor(R.color.colorOn))
                   mflashLightImg!!.setImageResource( R.drawable.ic_panelflashlight_on)
               } else {
                   mflashLight!!.setTextColor(resources.getColor(R.color.colorOff))
                   mflashLightImg!!.setImageResource(R.drawable.ic_panelflashlight_off)
               }
           } else {
               if (parameters?.flashMode == Camera.Parameters.FLASH_MODE_TORCH) {
                   mflashLight!!.setTextColor(resources.getColor(R.color.colorOn))
                   mflashLightImg!!.setImageResource(R.drawable.ic_panelflashlight_on)
               }
           }

        mflashLightImg!!.setOnClickListener {
            //NutDenPin()
            if (android.os.Build.VERSION.SDK_INT >= 27) {
                isopen = !isopen
                toggleFlashLight()
            }else{
                NutDenPin()
            }

        }
        //check current state wifi
        val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            mWifi!!.setTextColor(resources.getColor(R.color.colorOn))
            mWifiImg!!.setImageResource( R.drawable.ic_panelwifi_on)
        } else {
            mWifi!!.setTextColor(resources.getColor(R.color.colorOff))
            mWifiImg!!.setImageResource( R.drawable.ic_panelwifi_off)
        }
        var checkstatewifi = false
        mWifiImg!!.setOnClickListener {
            checkstatewifi = !checkstatewifi
            if (checkstatewifi) {
                wifiManager.isWifiEnabled = true
                //chinh cho icon wifi enable true
                mWifi!!.setTextColor(resources.getColor(R.color.colorOn))
                mWifiImg!!.setImageResource( R.drawable.ic_panelwifi_on)
            } else {
                if (wifiManager.isWifiEnabled) {
                    wifiManager.isWifiEnabled = false
                    //chinh cho wifi enable false
                    mWifi!!.setTextColor(resources.getColor(R.color.colorOff))
                    mWifiImg!!.setImageResource(R.drawable.ic_panelwifi_off)
                }
            }

            Log.d(contrast.TAG, "onclick control")
        }

        if (isAirplaneModeOn(this)) {
            mPlane!!.setTextColor(resources.getColor(R.color.colorOn))
            mPlaneImg!!.setImageResource( R.drawable.ic_panelplane_on)
        } else {
            mPlane!!.setTextColor(resources.getColor(R.color.colorOff))
            mPlaneImg!!.setImageResource(R.drawable.ic_panelplane_off)
        }
        mPlaneImg!!.setOnClickListener {
            NutMayBay()

        }
        mViewdialog?.setOnTouchListener(object : View.OnTouchListener {

            var startime: Long? = null
            var initX: Int? = null
            var initY: Int? = null
            var initTouchX: Float? = null
            var initTouchY: Float? = null
            var islongclick: Boolean? = null
            val timeTouchLongClick = 5000
            var isclick = false;

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        islongclick = true
                        Log.d(contrast.TAG, "assitive Touch down")
                        initTouchX = event.rawX
                        initTouchY = event.rawY
                        initX = layoutParams.x
                        initY = layoutParams.y
                        //set startime
                        startime = System.currentTimeMillis();

                    }
                    MotionEvent.ACTION_MOVE -> {

                    }
                    MotionEvent.ACTION_UP -> {
                        islongclick = false
                        val Xdiff = (event.rawX - initTouchX?.toInt()!!)
                        val Ydiff = (event.rawY - initTouchY?.toInt()!!)
                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        var timeDelay: Long = System.currentTimeMillis() - startime!!
                        if (Xdiff < 10 && Ydiff < 10 && timeDelay < 1000) {
                            Log.d(contrast.TAG, "one click time")
                            isclick = !isclick
                            if (isclick) {
                                Log.d(contrast.TAG, "hide layout control and show begin")
                                HideLayoutControl()
                            } else {
                                Log.d(contrast.TAG, "Return Layout Root with Touch")
                                //etc
                            }
                        }
                        return true
                    }
                }
                return false
            }

        })

    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.getContentResolver(),
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0;

    }

    private fun NutMayBay() {
        try {
            var intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
            HideLayoutControl()


        } catch (e: java.lang.Exception) {
            try {
                var intent = Intent("android.settings.WIRELESS_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
                HideLayoutControl()
            } catch (ex: java.lang.Exception) {

            }
        }
    }

    var isopen :Boolean=false
    var mCameraId: String? = null
    var mCameraManager: CameraManager? = null
    var stroboFrequency = 1000L

    fun toggleFlashLight() {

            mCameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                mCameraId = mCameraManager!!.getCameraIdList()[0]
                if (mCameraId != null) {
                    if(isopen) {
                        mflashLight!!.setTextColor(resources.getColor(R.color.colorOn))
                        mflashLightImg!!.setImageResource(R.drawable.ic_panelflashlight_on)
                        val stroboscope = Runnable {
                            create=true
                            mCameraManager!!.setTorchMode(mCameraId, true)
                                return@Runnable

                        }
                        stroboscope.run()
                    }else{
                        create=false
                        mCameraManager!!.setTorchMode(mCameraId, false)
                        mflashLight!!.setTextColor(resources.getColor(R.color.colorOff))
                        mflashLightImg!!.setImageResource(R.drawable.ic_panelflashlight_off)
                    }
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
                if(android.os.Build.VERSION.SDK_INT >= 28){
//                    mflashLight!!.setTextColor(resources.getColor(R.color.colorOn))
//                    mflashLightImg!!.setImageResource(R.drawable.ic_panelflashlight_on)
                    val stroboscope = Runnable {
                        mCameraManager!!.setTorchMode(mCameraId, true)
                        return@Runnable

                    }
                    stroboscope.run()
                }

            }


    }

    var cam: Camera? = null
    var parameters: Camera.Parameters? = null
    private fun NutDenPin() {

            var check = parameters?.flashMode
            if (!check.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                cam = Camera.open()
                parameters = cam!!.getParameters()
                parameters?.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                cam?.setParameters(parameters)
                cam?.startPreview()
                    mflashLight!!.setTextColor(resources.getColor(R.color.colorOn))
                    mflashLightImg!!.setImageResource(R.drawable.ic_panelflashlight_on)
                } else {
                try {
                    parameters?.flashMode = Camera.Parameters.FLASH_MODE_OFF
                    cam?.stopPreview()
                    cam?.release()
                    cam = null
                    mflashLight!!.setTextColor(resources.getColor(R.color.colorOff))
                    mflashLightImg!!.setImageResource(R.drawable.ic_panelflashlight_off)

                    } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Toast.makeText(getBaseContext(), "Exception flashLightOff", Toast.LENGTH_SHORT).show();
                }
            }
    }
    private fun NutChedoSang() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                var value = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS)
                value += 20
                if (value > 245) {
                    value = 1
                }
                if (value > 140) {
                    mLight!!.setTextColor(resources.getColor(R.color.colorOn))
                    mLightImg!!.setImageResource( R.drawable.ic_panellight_on)

                } else {
                    mLight!!.setTextColor(resources.getColor(R.color.colorOff))
                    mLightImg!!.setImageResource(R.drawable.ic_panellight_off)
                }

                android.provider.Settings.System.putInt(
                    this.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, value
                ); //<-- 1-225
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.setData(Uri.parse("package:" + this.getPackageName()))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        } else {
            var value = Settings.System.getInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS)
            value += 20
            if (value > 245) {
                value = 1
            }
            if (value > 140) {
                mLight!!.setTextColor(resources.getColor(R.color.colorOn))
                mLightImg!!.setImageResource( R.drawable.ic_panellight_on)

            } else {
                mLight!!.setTextColor(resources.getColor(R.color.colorOff))
                mLightImg!!.setImageResource(R.drawable.ic_panellight_off)
            }
            android.provider.Settings.System.putInt(
                this.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS, value
            ); //<-- 1-225
        }
    }

    private fun IsRotate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                val isrotate = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION)
                Log.e("ROTATE", "rotate " + isrotate)
                if (isrotate == 1) {
                    mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOn))
                    mRoteScreen
                    mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_on)
                    //seticon
                } else {
                    mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOff))
                    //setiocn
                    mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_off)
                }

            } else {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.setData(Uri.parse("package:" + this.getPackageName()))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        } else {
            val isrotate = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION)
            if (isrotate == 1) {
                //seticon
                mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOn))
                mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_on)
            } else {
                //setiocn
                mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOff))
                mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_off)
            }
        }
    }

    private fun NutxoayTudong() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                val isrotate = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION)

                if (isrotate == 1) {
                    mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOff))
                    Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0)
                    mRoteScreen
                    mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_off)
                    //seticon
                } else {
                    mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOn))
                    Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1)
                    //setiocn
                    mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_on)
                }

            } else {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.setData(Uri.parse("package:" + this.getPackageName()))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        } else {
            val isrotate = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION)

            if (isrotate == 1) {
                mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOff))
                mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_off)

                Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0)
                //seticon
            } else {
                mRoteScreen!!.setTextColor(resources.getColor(R.color.colorOn))
                Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1)
                //setiocn
                mRoteScreenImg!!.setImageResource( R.drawable.ic_panelrotate_on)


            }
        }
    }

    var mwindowmapplayout: WindowManager? = null;
    var mviewapplayout: View? = null
    var tableapplayout: ViewGroup? = null
    var likeapp1: ImageView? = null
    var likeapp2: ImageView? = null
    var likeapp3: ImageView? = null
    var likeapp4: ImageView? = null
    var likeapp5: ImageView? = null
    var likeapp6: ImageView? = null
    var likeapp7: ImageView? = null
    var likeapp8: ImageView? = null

    var nameapp1: TextView? = null
    var nameapp2: TextView? = null
    var nameapp3: TextView? = null
    var nameapp4: TextView? = null
    var nameapp5: TextView? = null
    var nameapp6: TextView? = null
    var nameapp7: TextView? = null
    var nameapp8: TextView? = null

    //1
    fun showlayoutApp() {
        btntouch?.visibility = View.GONE
        fullLayout?.visibility = View.GONE
        mviewapplayout = LayoutInflater.from(this).inflate(R.layout.dialoglikeapp, null);
        val LAYOUT_FLAG: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
        }
        mwindowmapplayout = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.CENTER
        layoutParams.x = 0
        layoutParams.y = 0
        tableapplayout = mviewapplayout!!.findViewById(R.id.dialog_layoutapptable)
        setbackgroundAppPackage()
        mwindowmapplayout!!.addView(mviewapplayout, layoutParams)

        mviewapplayout?.setOnTouchListener(object : View.OnTouchListener {

            var startime: Long? = null
            var initX: Int? = null
            var initY: Int? = null
            var initTouchX: Float? = null
            var initTouchY: Float? = null
            var islongclick: Boolean? = null
            val timeTouchLongClick = 5000
            var isclick = false;

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        islongclick = true
                        Log.d(contrast.TAG, "assitive Touch down")
                        initTouchX = event.rawX
                        initTouchY = event.rawY
                        initX = layoutParams.x
                        initY = layoutParams.y
                        //set startime
                        startime = System.currentTimeMillis();

                    }
                    MotionEvent.ACTION_MOVE -> {

                    }
                    MotionEvent.ACTION_UP -> {
                        islongclick = false
                        val Xdiff = (event.rawX - initTouchX?.toInt()!!)
                        val Ydiff = (event.rawY - initTouchY?.toInt()!!)
                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        var timeDelay: Long = System.currentTimeMillis() - startime!!
                        if (Xdiff < 10 && Ydiff < 10 && timeDelay < 1000) {
                            mviewapplayout?.visibility = View.GONE
                            mviewmain?.visibility = View.VISIBLE
                            mFloatingView?.visibility = View.VISIBLE
                            btntouch?.visibility = View.VISIBLE

                        }
                        return true
                    }
                }
                return false
            }

        })
        likeapp1 = mviewapplayout!!.findViewById(R.id.app_1)
        likeapp2 = mviewapplayout!!.findViewById(R.id.app_2)
        likeapp3 = mviewapplayout!!.findViewById(R.id.app_3)
        likeapp4 = mviewapplayout!!.findViewById(R.id.app_4)
        likeapp5 = mviewapplayout!!.findViewById(R.id.app_5)
        likeapp6 = mviewapplayout!!.findViewById(R.id.app_6)
        likeapp7 = mviewapplayout!!.findViewById(R.id.app_7)
        likeapp8 = mviewapplayout!!.findViewById(R.id.app_8)
        nameapp1 = mviewapplayout!!.findViewById(R.id.name_app1)
        nameapp2 = mviewapplayout!!.findViewById(R.id.name_app2)
        nameapp3 = mviewapplayout!!.findViewById(R.id.name_app3)
        nameapp4 = mviewapplayout!!.findViewById(R.id.name_app4)
        nameapp5 = mviewapplayout!!.findViewById(R.id.name_app5)
        nameapp6 = mviewapplayout!!.findViewById(R.id.name_app6)
        nameapp7 = mviewapplayout!!.findViewById(R.id.name_app7)
        nameapp8 = mviewapplayout!!.findViewById(R.id.name_app8)
        CheckIconTextApp(1)
        CheckIconTextApp(2)
        CheckIconTextApp(3)
        CheckIconTextApp(4)
        CheckIconTextApp(5)
        CheckIconTextApp(6)
        CheckIconTextApp(7)
        CheckIconTextApp(8)
        var backmain: ImageView = mviewapplayout!!.findViewById(R.id.app_back_mainlayout)
        //btn out ra main //hiện layoutmain ẩn layout app
        backmain.setOnClickListener {
            mviewapplayout?.visibility = View.GONE

            btntouch?.visibility = View.VISIBLE
            fullLayout?.visibility = View.VISIBLE

        }
        likeapp1?.setOnClickListener {
            //kiểm tra nếu chưa được gắn app nào thì Sẽ mở mản hình  mói cho người dùng chọn ngược lại mở ứng dụng
            //đóng layout app và mở ứng dụng tương ứng,thu nhỏ còn FLoatview
            val model = db?.getbyIDApp(1)
            Checkpakagename(model?.packagename!!)
        }
        likeapp1?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(1)
            return@setOnLongClickListener true
        }
        likeapp2?.setOnClickListener {
            val model = db?.getbyIDApp(2)
            Checkpakagename(model?.packagename!!)
        }
        likeapp2?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(2)
            return@setOnLongClickListener true
        }
        likeapp3?.setOnClickListener {
            val model = db?.getbyIDApp(3)
            Checkpakagename(model?.packagename!!)
        }
        likeapp3?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(3)
            return@setOnLongClickListener true
        }
        likeapp4?.setOnClickListener {
            val model = db?.getbyIDApp(4)
            Checkpakagename(model?.packagename!!)
        }
        likeapp4?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(4)
            return@setOnLongClickListener true
        }
        likeapp5?.setOnClickListener {
            val model = db?.getbyIDApp(5)
            Checkpakagename(model?.packagename!!)
        }
        likeapp5?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(5)
            return@setOnLongClickListener true
        }
        likeapp6?.setOnClickListener {
            val model = db?.getbyIDApp(6)
            Checkpakagename(model?.packagename!!)
        }
        likeapp6?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(6)
            return@setOnLongClickListener true
        }
        likeapp7?.setOnClickListener {
            val model = db?.getbyIDApp(7)
            Checkpakagename(model?.packagename!!)
        }
        likeapp7?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(7)
            return@setOnLongClickListener true
        }
        likeapp8?.setOnClickListener {
            val model = db?.getbyIDApp(8)
            Checkpakagename(model?.packagename!!)
        }
        likeapp8?.setOnLongClickListener {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(100)
            NutChonUngdung(8)
            return@setOnLongClickListener true
        }
    }

    private fun HideLayoutControl() {
        mViewdialog?.visibility = View.GONE
        btntouch?.visibility = View.VISIBLE
        mFloatingView?.visibility = View.VISIBLE
    }

    private fun HideTouch3D() {
        btntouch?.visibility = View.GONE
        fullLayout?.visibility = View.GONE

    }

    fun removeView() {
        if (mFloatingView != null) {
            mwindowManager?.removeView(mFloatingView)
            mIsFloatingViewAttached = false
        }
    }
    var isFullScreen = false
    var heightS = 0;
    private fun createHelperWnd() {
        val wm = getSystemService(Service.WINDOW_SERVICE) as WindowManager
        val p = WindowManager.LayoutParams()
        p.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
        p.gravity = Gravity.RIGHT or Gravity.TOP
        p.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        p.width = 1
        p.height = WindowManager.LayoutParams.MATCH_PARENT
        p.format = PixelFormat.TRANSPARENT
        var helperWnd: View? = null
        helperWnd = View(this) //View helperWnd;
        wm.addView(helperWnd, p)
        val vto = helperWnd.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (heightS == helperWnd.getHeight()) {
                    isFullScreen = true
                } else {
                    isFullScreen = false
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // set action
        if (intent?.action.equals("com.exemple.assistivetouch.ShowLayoutapp")) {
//            btntouch?.visibility = View.GONE
//            fullLayout?.visibility = View.GONE
//            mviewapplayout?.visibility=View.VISIBLE
            showlayoutApp()
        }
        // UpdateDeviceToApp()
        if (!mIsFloatingViewAttached!!) {

            //  mwindowManager?.addView(mFloatingView, mFloatingView?.getLayoutParams());
        }
        return Service.START_NOT_STICKY
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.getAction()
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        mbluetooth!!.setTextColor(resources.getColor(R.color.colorOff))
                        mbluetoothImg!!.setImageResource( R.drawable.ic_panelbluetooth_off)
                    }
                    BluetoothAdapter.STATE_ON -> {
                        mbluetooth!!.setTextColor(resources.getColor(R.color.colorOn))
                        mbluetoothImg!!.setImageResource(R.drawable.ic_panelbluetooth_on)
                    }
                }
            }
            if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
                Log.d("thiennu", "ok")
                val networkInfo: NetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && !networkInfo.isConnected()) {
                    mWifi!!.setTextColor(resources.getColor(R.color.colorOff))
                    mWifiImg!!.setImageResource(R.drawable.ic_panelwifi_off)
                } else {
                    mWifi!!.setTextColor(resources.getColor(R.color.colorOn))
                    mWifiImg!!.setImageResource(R.drawable.ic_panelwifi_on)
                }
            }
            if (action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                if (isAirplaneModeOn(context)) {
                    mPlane!!.setTextColor(resources.getColor(R.color.colorOn))
                    mPlaneImg!!.setImageResource( R.drawable.ic_panelplane_on)
                } else {
                    mPlane!!.setTextColor(resources.getColor(R.color.colorOff))
                    mPlaneImg!!.setImageResource(R.drawable.ic_panelplane_off)
                }
            }
        }
    }


    override fun onDestroy() {
        Log.d(contrast.TAG, "onDestroy Service")
        removeView()

        super.onDestroy()

    }

    private fun IsBlueTouch(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getResources()?.getString(R.string.warning_notsuportBluetooth), Toast.LENGTH_SHORT)
                .show()
            return false
        } else {
            val isEnabled = bluetoothAdapter.isEnabled
            if (!isEnabled) {
                mbluetooth!!.setTextColor(resources.getColor(R.color.colorOff))
                mbluetoothImg!!.setImageResource( R.drawable.ic_panelbluetooth_off)
                return false
            } else if (isEnabled) {
                mbluetooth!!.setTextColor(resources.getColor(R.color.colorOn))
                mbluetoothImg!!.setImageResource( R.drawable.ic_panelbluetooth_on)
                return true
            }
            // No need to change bluetooth state
        }
        return false
    }

    fun setBluetooth(enable: Boolean): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getResources()?.getString(R.string.warning_notsuportBluetooth), Toast.LENGTH_SHORT)
                .show()
        } else {
            val isEnabled = bluetoothAdapter.isEnabled
            if (enable && !isEnabled) {
                mbluetooth!!.setTextColor(resources.getColor(R.color.colorOn))
                mbluetoothImg!!.setImageResource(R.drawable.ic_panelbluetooth_on)
                return bluetoothAdapter.enable()
            } else if (!enable && isEnabled) {
                mbluetooth!!.setTextColor(resources.getColor(R.color.colorOff))
                mbluetoothImg!!.setImageResource(R.drawable.ic_panelbluetooth_off)
                return bluetoothAdapter.disable()
            }
            // No need to change bluetooth state
        }
        return true
    }

    fun setbackgoundisSave() {
        if (AppPreferences.colorbackground != 0) {
            //set color backgound
            var shape: GradientDrawable = GradientDrawable()
            shape.cornerRadius = 20.0f
            shape.setColor(AppPreferences.colorbackground)
            tablelayout!!.background = shape
        }
    }

    fun setbackgoundControl() {
        if (AppPreferences.colorbackground != 0) {
            //set color backgound
            var shape: GradientDrawable = GradientDrawable()
            shape.cornerRadius = 20.0f
            shape.setColor(AppPreferences.colorbackground)
            mViewControl!!.background = shape
        }
    }

    fun setbackgroundAppPackage() {
        if (AppPreferences.colorbackground != 0) {
            //set color backgound
            var shape: GradientDrawable = GradientDrawable()
            shape.cornerRadius = 20.0f
            shape.setColor(AppPreferences.colorbackground)
            tableapplayout!!.background = shape
        }
    }

    /*
     tất cả các hàm sử dụng cho các nút ở layoutmain
    */
    private fun NutChonUngdung(id: Int) {
        //dimiss showlayotapp
        //dimislayoutmain
        //hide floatbuton
        mviewapplayout!!.visibility = View.GONE
        btntouch?.visibility = View.VISIBLE
        HideLayoutmain()
        var intent: Intent = Intent(this, PakageAppActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(contrast.PUTIDAPP, id)
        startActivity(intent)
    }
    //2
    private fun NutQuaylai() {
        if (AppPreferences.isAccesbisilityconnected.equals(false)) {
            Toast.makeText(
                this,
                getResources()?.getString(R.string.warning_Assibility),
                Toast.LENGTH_SHORT
            ).show()
            val intent: Intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            var serviceIntent = Intent(this, MyAssibilityService::class.java)
            serviceIntent.setAction("com.exampke.assistivetouch.NutQuayLai")
            startService(serviceIntent);
        }
    }

    //3
    private fun NutDanhiem() {
        HideLayoutmain()
        if (AppPreferences.isAccesbisilityconnected.equals(false)) {
            Toast.makeText(
                this,
                getResources()?.getString(R.string.warning_Assibility),
                Toast.LENGTH_SHORT
            ).show()
            val intent: Intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            var serviceIntent = Intent(this, MyAssibilityService::class.java)
            serviceIntent.setAction("com.exampke.assistivetouch.Nutdanhiem")
            startService(serviceIntent);
        }

    }

    //6
    var stateringmode = false

    private fun NutChedoam() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (AppPreferences.noti) {
                Log.d(contrast.TAG, "1")
                stateringmode = !stateringmode
                val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                if (stateringmode) {
                    btnringmode!!.setText(getResources()?.getString(R.string.panel_main_ringmodeon))
                    audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                } else {
                    btnringmode!!.setText(getResources()?.getString(R.string.panel_main_ringmodeoff))
                    audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                }
            } else {
                Log.d(contrast.TAG, "2")
                var intent = Intent(this, OnresultAcitvity2::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        } else {
            stateringmode = !stateringmode
            val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (stateringmode) {
                btnringmode!!.setText(getResources()?.getString(R.string.panel_main_ringmodeon))
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            } else {
                btnringmode!!.setText(getResources()?.getString(R.string.panel_main_ringmodeoff))
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
        }
    }

    //4
    private fun NutVeTrangchu() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
        HideLayoutmain()
    }

    //5
    private fun NutHienThiThongbao() {
        HideLayoutmain()
        if (AppPreferences.isAccesbisilityconnected.equals(false)) {
            Toast.makeText(
                this,
                getResources()?.getString(R.string.warning_Assibility),
                Toast.LENGTH_SHORT
            ).show()
            val intent: Intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            var serviceIntent = Intent(this, MyAssibilityService::class.java)
            serviceIntent.setAction("com.exampke.assistivetouch.NutThongbao")
            startService(serviceIntent);
        }
    }

    private fun NOTIFICATION_BUTTON() {

    }

    var devicePolicyManager: DevicePolicyManager? = null
    var componentName: ComponentName? = null

    //7
    private fun Nutdieukhien() {
        misDialogviewAttached = true
            ShowdialogControl()

    }

    //8
    private fun NutKhoaManHinh() {
        val intent: Intent = Intent(this, OnResultAcitvity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        devicePolicyManager = getSystemService(Service.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        componentName = ComponentName(this, MyAdmin::class.java)
        var isActive = devicePolicyManager!!.isAdminActive(componentName)
        if (isActive) {
            devicePolicyManager?.lockNow()
        } else {


        }
    }


    private fun CheckEvent(idevent: Int) {
        when (idevent) {
            1 -> {
                showlayoutApp()
            }
            2 -> {
                NutQuaylai()
            }
            3 -> {
                NutDanhiem()
            }
            4 -> {
                NutVeTrangchu()
            }
            5 -> {
                NutHienThiThongbao()
            }
            6 -> {
                NutChedoam()
            }
            7 -> {
                Nutdieukhien()
            }
            8 -> {
                NutKhoaManHinh()
            }
            10 -> {
                NutTangam()
            }
            9 -> {
                Nutgiamam()
            }
        }

    }

    private fun Nutgiamam() {
        var audio: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    private fun NutTangam() {
        var audio: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    private fun CheckIconText(idbutton: Int) {
        when (idbutton) {
            1 -> {
                //lấy csdl rồi set cho cái control Nút ứng dụng  cũ

                val eventmodel = db?.getbyID(1)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnapp?.setText(eventmodel?.textevent)
                    btnappImg?.setImageResource(eventmodel?.iconevent!!)
                }
            }
            2 -> {
                //lấy csdl rồi set cho cái control Nút Quay Lại cũ
                val eventmodel = db?.getbyID(2)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnbackpress?.setText(eventmodel?.textevent)
                    btnbackpressImg?.setImageResource(eventmodel?.iconevent!!)
                }

                return
            }
            3 -> {
                //lấy csdl rồi set cho cái control Nút Đa nhiệm  cũ
                val eventmodel = db?.getbyID(3)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnRescent?.setText(eventmodel?.textevent)
                    btnRescentImg?.setImageResource(eventmodel?.iconevent!!)
                }

            }
            4 -> {
                //lấy csdl rồi set cho cái control Nút Trang Chủ  cũ
                val eventmodel = db?.getbyID(4)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnhome?.setText(eventmodel?.textevent)
                    btnhomeImg?.setImageResource(eventmodel?.iconevent!!)
                }

            }
            5 -> {
                //lấy csdl rồi set cho cái control Nút Thông báo  cũ
                val eventmodel = db?.getbyID(5)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnshowNotification?.setText(eventmodel?.textevent)
                    btnshowNotificationImg?.setImageResource(eventmodel?.iconevent!!)
                }

            }
            6 -> {
                //lấy csdl rồi set cho cái control Nút Chế độ âm  cũ
                val eventmodel = db?.getbyID(6)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnringmode?.setText(eventmodel?.textevent)
                    btnringmodeImg?.setImageResource(eventmodel?.iconevent!!)
                }
                return
            }
            7 -> {
                //lấy csdl rồi set cho cái control Nút Điều khiển  cũ
                val eventmodel = db?.getbyID(7)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnControl?.setText(eventmodel?.textevent)
                    btnControlImg?.setImageResource(eventmodel?.iconevent!!)
                }
                return
            }
            8 -> {
                //lấy csdl rồi set cho cái control Nút Khóa màn hình  cũ
                val eventmodel = db?.getbyID(8)
                if (eventmodel?.iconevent != 0) {
                    //thay icon , Thay text
                    btnlock?.setText(eventmodel?.textevent)
                    btnlockImg?.setImageResource(eventmodel?.iconevent!!)
                }
                return
            }
        }

    }
    // viết hàm check để seticon và set sự kiện mở ứng dụng trong onclick

    private fun Checkpakagename(namepakage: String) {
        if (!namepakage.equals("null")) {
            mviewapplayout!!.visibility = View.GONE
            btntouch?.visibility = View.VISIBLE
            HideLayoutmain()
            var intent: Intent = getPackageManager().getLaunchIntentForPackage(namepakage)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun CheckIconTextApp(id: Int) {
        when (id) {
            1 -> {
                var model = db?.getbyIDApp(1)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp1?.setImageDrawable(icon)
                    nameapp1?.setText(model?.nameapp)
                } else {
                    Toast.makeText(this, getResources()?.getString(R.string.warning_choiseApp), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            2 -> {
                var model = db?.getbyIDApp(2)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp2?.setImageDrawable(icon)
                    nameapp2?.setText(model?.nameapp)
                } else {
                    Toast.makeText(this, getResources()?.getString(R.string.warning_choiseApp), Toast.LENGTH_SHORT)
                        .show()
                }

            }
            3 -> {
                var model = db?.getbyIDApp(3)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp3?.setImageDrawable(icon)
                    nameapp3?.setText(model?.nameapp)
                } else {
                    Toast.makeText(this, getResources()?.getString(R.string.warning_choiseApp), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            4 -> {
                var model = db?.getbyIDApp(4)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp4?.setImageDrawable(icon)
                    nameapp4?.setText(model?.nameapp)
                }

            }
            5 -> {
                var model = db?.getbyIDApp(5)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp5?.setImageDrawable(icon)
                    nameapp5?.setText(model?.nameapp)
                }
            }
            6 -> {
                var model = db?.getbyIDApp(6)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp6?.setImageDrawable(icon)
                    nameapp6?.setText(model?.nameapp)
                }
            }
            7 -> {
                var model = db?.getbyIDApp(7)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp7?.setImageDrawable(icon)
                    nameapp7?.setText(model?.nameapp)
                }
            }
            8 -> {
                var model = db?.getbyIDApp(8)
                if (!model?.packagename.equals("null")) {
                    var icon = this.getPackageManager()?.getApplicationIcon(model?.packagename)
                    likeapp8?.setImageDrawable(icon)
                    nameapp8?.setText(model?.nameapp)
                }
            }
        }
    }
}