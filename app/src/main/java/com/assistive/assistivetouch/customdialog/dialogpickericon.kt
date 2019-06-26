package com.assistive.assistivetouch.customdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.view.View
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.datasave.AppPreferences
import kotlinx.android.synthetic.main.dialogpickericon.*
import java.io.ByteArrayOutputStream

import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import com.assistive.assistivetouch.Interface.ChangeTouchlistener
import com.assistive.assistivetouch.Interface.changetouchdemolistener


class dialogpickericon(context: Context, changeTouchlistener: ChangeTouchlistener, changetouchdemolistener: changetouchdemolistener) : Dialog(context) ,View.OnClickListener{
    private var isok: Int=0
    var icon1:ImageView?=null
    var icon2:ImageView?=null
    var icon3:ImageView?=null
    var icon4:ImageView?=null
    var icon5:ImageView?=null
    var icon6:ImageView?=null
    var icon7:ImageView?=null
    var icon8:ImageView?=null
    var icon9:ImageView?=null
    var checkicon1:ImageView?=null
    var checkicon2:ImageView?=null
    var checkicon3:ImageView?=null
    var checkicon4:ImageView?=null
    var checkicon5:ImageView?=null
    var checkicon6:ImageView?=null
    var checkicon7:ImageView?=null
    var checkicon8:ImageView?=null
    var checkicon9:ImageView?=null
    var btn:Button?=null
    var changeTouchlistener: ChangeTouchlistener?=null
    var changetouchdemolistener: changetouchdemolistener?=null
    init
    {
        this.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialogpickericon)
        this.changeTouchlistener=changeTouchlistener
        this.changetouchdemolistener=changetouchdemolistener
        icon1= findViewById(R.id.icon_1)
        setTitle("")
        icon1!!.setOnClickListener(this)
        icon2= findViewById(R.id.icon_2)
        icon2!!.setOnClickListener(this)
        icon3= findViewById(R.id.icon_3)
        icon3!!.setOnClickListener(this)
        icon4= findViewById(R.id.icon_4)
        icon4!!.setOnClickListener(this)
        icon5= findViewById(R.id.icon_5)
        icon5!!.setOnClickListener(this)
        icon6= findViewById(R.id.icon_6)
        icon6!!.setOnClickListener(this)
        icon7= findViewById(R.id.icon_7)
        icon7!!.setOnClickListener(this)
        icon8= findViewById(R.id.icon_8)
        icon8!!.setOnClickListener(this)
        icon9= findViewById(R.id.icon_9)
        icon9!!.setOnClickListener(this)
        checkicon1= findViewById(R.id.iconcheck1)
        checkicon2= findViewById(R.id.iconcheck2)
        checkicon3= findViewById(R.id.iconcheck3)
        checkicon4= findViewById(R.id.iconcheck4)
        checkicon5= findViewById(R.id.iconcheck5)
        checkicon6= findViewById(R.id.iconcheck6)
        checkicon7= findViewById(R.id.iconcheck7)
        checkicon8= findViewById(R.id.iconcheck8)
        checkicon9= findViewById(R.id.iconcheck9)
        btn= findViewById(R.id.icon_okbtn)
        btn!!.setOnClickListener(this)
        checkIconSelected()
        if(AppPreferences.icon.equals("")){
            val bitmap = (icon1?.getDrawable() as BitmapDrawable).bitmap
            AppPreferences.icon=encodeTobase64(bitmap)
            AppPreferences.iconnumber=1
        }

    }

    private fun checkIconSelected() {
        if(AppPreferences.iconnumber==1){
            checkicon1?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==2){
            checkicon2?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==3){
            checkicon3?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==4){
            checkicon4?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==5){
            checkicon5?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==6){
            checkicon6?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==7){
            checkicon7?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==8){
            checkicon8?.visibility=View.VISIBLE
        }
        if(AppPreferences.iconnumber==9){
            checkicon9?.visibility=View.VISIBLE
        }
    }

    companion object
    {
        var ictouch: ArrayList<Bitmap> = ArrayList()
    }
    override fun onStop() {
        super.onStop()
        ictouch= ArrayList()
    }
    override fun onClick(v: View?) {

        when(v?.id){
            R.id.icon_1 ->    {
                VISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon1?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
//                val bitmap = (icon1?.getDrawable() as BitmapDrawable).bitmap
//                changeTouchlistener?.OnIconChanger(bitmap)
                isok=1
            }
            R.id.icon_2 ->    {
                VISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon2?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=2
            }
            R.id.icon_3 ->    {
                VISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon3?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=3
            }
            R.id.icon_4 ->    {
                VISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon4?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=4
            }
            R.id.icon_5 ->    {
                VISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon5?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=5
            }
            R.id.icon_6 ->    {
                VISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon6?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=6
            }
            R.id.icon_7 ->    {
                VISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon7?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=7
            }
            R.id.icon_8 ->    {
                VISIBLEVIEW(checkicon8)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon9)
                val bitmap = (icon8?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=8
            }
            R.id.icon_9 ->    {
                VISIBLEVIEW(checkicon9)
                INVISIBLEVIEW(checkicon1)
                INVISIBLEVIEW(checkicon2)
                INVISIBLEVIEW(checkicon3)
                INVISIBLEVIEW(checkicon4)
                INVISIBLEVIEW(checkicon5)
                INVISIBLEVIEW(checkicon6)
                INVISIBLEVIEW(checkicon7)
                INVISIBLEVIEW(checkicon8)
                val bitmap = (icon9?.getDrawable() as BitmapDrawable).bitmap
                changetouchdemolistener?.OnIconChangerdemo(bitmap)
                isok=9
            }
            R.id.icon_okbtn->{

                if(isok!=0){
                    boolean=true
                if(isok==1){
                    val bitmap = (icon1?.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=1
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==2){
                    val bitmap = (icon_2.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=2
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==3){
                    val bitmap = (icon_3.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=3
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==4){
                    val bitmap = (icon_4.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=4
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==5){
                    val bitmap = (icon_5.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=5
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==6){
                    val bitmap = (icon_6.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=6
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==7){
                    val bitmap = (icon_7.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=7
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==8){
                    val bitmap = (icon_8.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=8
                    changeTouchlistener?.OnIconChanger(bitmap)
                }else if(isok==9){
                    val bitmap = (icon_9.getDrawable() as BitmapDrawable).bitmap
                    AppPreferences.icon=encodeTobase64(bitmap)
                    AppPreferences.iconnumber=9
                    changeTouchlistener?.OnIconChanger(bitmap)
                }
             }
                dismiss()
            }
        }
    }
    var boolean:Boolean=false

    private fun VISIBLEVIEW(checkicon: ImageView?) {
        checkicon?.visibility=View.VISIBLE

    }

    private fun INVISIBLEVIEW(checkicon: ImageView?) {
        checkicon?.visibility=View.INVISIBLE
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
    override fun dismiss() {
        //goi interface va set lai dl
        changeTouchlistener?.OnIconChanger(decodeBase64(AppPreferences.icon))
        super.dismiss()
    }
    override fun onBackPressed() {
        changeTouchlistener?.OnIconChanger(decodeBase64(AppPreferences.icon))
        super.onBackPressed()
    }

}