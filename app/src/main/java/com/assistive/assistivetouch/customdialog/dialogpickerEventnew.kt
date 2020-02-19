package com.assistive.assistivetouch.customdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable

import android.view.View
import android.view.Window
import android.widget.ImageView
import com.assistive.assistivetouch.Interface.ChangelayoutListener

import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.datasave.Database
import com.assistive.assistivetouch.model.Eventmodel
import kotlinx.android.synthetic.main.dialogpickerevent.*

class dialogpickerEventnew(context: Context,id: Int,changelayoutListener: ChangelayoutListener) : Dialog(context), View.OnClickListener {
    var changelayoutListeners: ChangelayoutListener?=null

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.event1_img -> {
                // lưu sự kiện event 1 , lưu icon 1 , reset lại layout Picker(Icon,Text)
                //csdl  sẽ  lưu 8 hàng update event với id =1, icon null, Text  bằng ứng dụng event call nè
                database.updateEventFake(Eventmodel(1, R.drawable.ic_panelapp,context.getResources().getString(R.string.panel_main_app)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event2_img ->{
                database.updateEventFake(Eventmodel(2,R.drawable.ic_panelbacksytem,context.getResources().getString(R.string.panel_main_back)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event3_img ->{
                database.updateEventFake(Eventmodel(3,R.drawable.ic_panelrecents,context.getResources().getString(R.string.panel_main_Recents)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event4_img ->{
                database.updateEventFake(Eventmodel(4, R.drawable.ic_panelhome,context.getResources().getString(R.string.panel_main_home)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event5_img ->{
                database.updateEventFake(Eventmodel(5,R.drawable.ic_panelnotification,context.getResources().getString(R.string.panel_main_notification)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event6_img ->{
                database.updateEventFake(Eventmodel(6, R.drawable.ic_panelringmode,context.getResources().getString(R.string.panel_main_ringmode)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event7_img ->{
                database.updateEventFake(Eventmodel(7,R.drawable.ic_panelcontrol,context.getResources().getString(R.string.panel_main_control)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event8_img ->{
                database.updateEventFake(Eventmodel(8,R.drawable.ic_panellock,context.getResources().getString(R.string.panel_main_lockscreen)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event9_img ->{
                database.updateEventFake(Eventmodel(9,R.drawable.ic_volumedown,context.getResources().getString(R.string.panel_main_lowsound)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event10_img ->{
                database.updateEventFake(Eventmodel(10,R.drawable.ic_volumeup,context.getResources().getString(R.string.panel_main_upsound)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            R.id.event11_img ->{
                database.updateEventFake(Eventmodel(11,R.drawable.ic_screenshot,context.getResources().getString(R.string.Screnshot)),idbutton!!)
                changelayoutListeners?.callbacks()
                dismiss()
            }
            
        }
    }

    var event1:ImageView?=null
    var event2:ImageView?=null
    var event3:ImageView?=null
    var event4:ImageView?=null
    var event5:ImageView?=null
    var event6:ImageView?=null
    var event7:ImageView?=null
    var event8:ImageView?=null
    var event9:ImageView?=null
    var event10:ImageView?=null
    var event11:ImageView?=null
    var database:Database= Database(getContext())
    var idbutton:Int ?=null
    init {
        this.window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialogpickerevent)
        this.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.idbutton=id
        this.changelayoutListeners=changelayoutListener
        val face = Typeface.createFromAsset(
            context.assets,
            "fonts/SanFranciscoDisplay_Bold_1.otf"
        )
        title_dialogevent.setTypeface(face)
        event1=findViewById(R.id.event1_img)
        event2=findViewById(R.id.event2_img)
        event3=findViewById(R.id.event3_img)
        event4=findViewById(R.id.event4_img)
        event5=findViewById(R.id.event5_img)
        event6=findViewById(R.id.event6_img)
        event7=findViewById(R.id.event7_img)
        event8=findViewById(R.id.event8_img)
        event9=findViewById(R.id.event9_img)
        event10=findViewById(R.id.event10_img)
       event11=findViewById(R.id.event11_img)
        event1!!.setOnClickListener(this)
        event2!!.setOnClickListener(this)
        event3!!.setOnClickListener(this)
        event4!!.setOnClickListener(this)
        event5!!.setOnClickListener(this)
        event6!!.setOnClickListener(this)
        event7!!.setOnClickListener(this)
        event8!!.setOnClickListener(this)
        event9!!.setOnClickListener(this)
        event10!!.setOnClickListener(this)
        event11!!.setOnClickListener(this)
    }
}