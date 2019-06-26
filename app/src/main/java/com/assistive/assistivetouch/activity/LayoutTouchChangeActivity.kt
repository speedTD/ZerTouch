package com.assistive.assistivetouch.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.AppCompatImageButton
import android.util.Log
import android.widget.TextView
import com.assistive.assistivetouch.Interface.ChangelayoutListener
import com.assistive.assistivetouch.until.contrast
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.customdialog.dialogpickerEventnew
import com.assistive.assistivetouch.datasave.Database
import com.assistive.assistivetouch.model.Eventmodel
import com.assistive.assistivetouch.until.Ads
import kotlinx.android.synthetic.main.activity_layout_touch_change.*
import kotlinx.android.synthetic.main.activity_layout_touch_change.layout_ads
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class LayoutTouchChangeActivity : AppCompatActivity() , ChangelayoutListener {
    override fun callbacks() {
        checkIconFake(1)
        checkIconFake(2)
        checkIconFake(3)
        checkIconFake(4)
        checkIconFake(5)
        checkIconFake(6)
        checkIconFake(7)
        checkIconFake(8)
        //load full Screen
        if(Random()){
            Ads.loadFullScreenAds(this)
        }
    }
    fun Random () :Boolean{
        var ran :Random =Random
        return ran.nextBoolean()
    }
    var data: Database = Database(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_touch_change)
        var actionBar=supportActionBar
        actionBar?.displayOptions= ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.toolbarlayout)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setTitle(getResources()?.getString(R.string.title_changelayout))
        val textviewTitle : TextView = findViewById(R.id.tvTitle)
        textviewTitle.text =getResources()?.getString( R.string.title_changelayout)
        val toolbarbackbtn : AppCompatImageButton = this.findViewById(R.id.toolbar_back)
        toolbarbackbtn.setOnClickListener {
            finish()
        }
        val toolbarsavebtn : AppCompatImageButton = this.findViewById(R.id.toolbar_save)
        toolbarsavebtn.setOnClickListener {
            SaveEventToDatabase()
            finish()
        }
        //showdialog cho người dùng chọn sự kiện khác
        checkIcon(1)
        checkIcon(2)
        checkIcon(3)
        checkIcon(4)
        checkIcon(5)
        checkIcon(6)
        checkIcon(7)
        checkIcon(8)
        layout_ungdung_img.setOnClickListener {
            showdialog(1)
        }
        layout_quaylai_img.setOnClickListener {
            showdialog(2)
        }
        layout_danhiem_img.setOnClickListener {
            //get xem là cái vị trí của button đã đc thay đổi trong db chưa nếu null thì chạy phương thức mặc định
            //khác null thì gọi vào fun check phương thức theo id  Nút
            showdialog(3)
        }
        layout_manhinhchinh_img.setOnClickListener {
            showdialog(4)
        }
        layout_thongbao_img.setOnClickListener {
            showdialog(5)
        }
        layout_chedoam_img.setOnClickListener {
            showdialog(6)
        }
        layout_dieukhien_img.setOnClickListener {
            showdialog(7)
        }
        layout_khoamanhinh_img.setOnClickListener {
            showdialog(8)
        }
        Ads.loadBannerAds(this, layout_ads)
    }

    private fun SaveEventToDatabase() {
        Log.d("thiennu","Change save event")
//        data.updateEvent(Eventmodel(1, R.drawable.ic_panelapp,getResources().getString(R.string.panel_main_app)),idbutton!!)
        val eventmodel1: Eventmodel = data.getbyIDFake(1)!!
        data.updateEvent(Eventmodel(eventmodel1.idevent,eventmodel1.iconevent!!,eventmodel1.textevent),1)
        val eventmodel2: Eventmodel = data.getbyIDFake(2)!!
        data.updateEvent(Eventmodel(eventmodel2.idevent,eventmodel2.iconevent!!,eventmodel2.textevent),2)
        val eventmodel3: Eventmodel = data.getbyIDFake(3)!!
        data.updateEvent(Eventmodel(eventmodel3.idevent,eventmodel3.iconevent!!,eventmodel3.textevent),3)
        val eventmodel4: Eventmodel = data.getbyIDFake(4)!!
        data.updateEvent(Eventmodel(eventmodel4.idevent,eventmodel4.iconevent!!,eventmodel4.textevent),4)
        val eventmodel5: Eventmodel = data.getbyIDFake(5)!!
        data.updateEvent(Eventmodel(eventmodel5.idevent,eventmodel5.iconevent!!,eventmodel5.textevent),5)
        val eventmodel6: Eventmodel = data.getbyIDFake(6)!!
        data.updateEvent(Eventmodel(eventmodel6.idevent,eventmodel6.iconevent!!,eventmodel6.textevent),6)
        val eventmodel7: Eventmodel = data.getbyIDFake(7)!!
        data.updateEvent(Eventmodel(eventmodel7.idevent,eventmodel7.iconevent!!,eventmodel7.textevent),7)
        val eventmodel8: Eventmodel = data.getbyIDFake(8)!!
        data.updateEvent(Eventmodel(eventmodel8.idevent,eventmodel8.iconevent!!,eventmodel8.textevent),8)
    }

    fun checkIcon(vitri: Int) {
        when (vitri) {
            1 -> {
                //seticon new

                val eventmodel: Eventmodel = data.getbyID(1)!!
                if (eventmodel.iconevent != 0) {
                    layout_ungdung.setText(eventmodel.textevent)
                    layout_ungdung_img.setImageResource( eventmodel.iconevent!!)
                }
            }
            2 -> {
                //seticon new
                val eventmodel: Eventmodel = data.getbyID(2)!!
                if (eventmodel.iconevent != 0) {
                    layout_quaylai.setText(eventmodel.textevent)
                    layout_quaylai_img?.setImageResource( eventmodel.iconevent!!)
                    Log.d(contrast.TAG, eventmodel.textevent)
                }
            }
            3 -> {
                //seticonnew
                val eventmodel: Eventmodel = data.getbyID(3)!!
                if (eventmodel.iconevent != 0) {
                    layout_danhiem.setText(eventmodel.textevent)
                    layout_danhiem_img?.setImageResource( eventmodel.iconevent!!)

                    Log.d(contrast.TAG, eventmodel.textevent)
                }
            }
            4 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyID(4)!!
                if (eventmodel.iconevent != 0) {
                    layout_manhinhchinh.setText(eventmodel.textevent)
                    layout_manhinhchinh_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            5 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyID(5)!!
                if (eventmodel.iconevent != 0) {
                    layout_thongbao.setText(eventmodel.textevent)
                    layout_thongbao_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            6 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyID(6)!!
                if (eventmodel.iconevent != 0) {
                    layout_chedoam.setText(eventmodel.textevent)
                    layout_chedoam_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            7 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyID(7)!!
                if (eventmodel.iconevent != 0) {
                    layout_dieukhien.setText(eventmodel.textevent)
                    layout_dieukhien_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            8 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyID(8)!!
                if (eventmodel.iconevent != 0) {
                    layout_khoamanhinh.setText(eventmodel.textevent)
                    layout_khoamanhinh_img?.setImageResource( eventmodel.iconevent!!)

                }
            }

        }
    }

    fun checkIconFake(vitri: Int) {
        when (vitri) {
            1 -> {
                //seticon new

                val eventmodel: Eventmodel = data.getbyIDFake(1)!!
                if (eventmodel.iconevent != 0) {
                    layout_ungdung.setText(eventmodel.textevent)
                    layout_ungdung_img.setImageResource( eventmodel.iconevent!!)
                }
            }
            2 -> {
                //seticon new
                val eventmodel: Eventmodel = data.getbyIDFake(2)!!
                if (eventmodel.iconevent != 0) {
                    layout_quaylai.setText(eventmodel.textevent)
                    layout_quaylai_img?.setImageResource( eventmodel.iconevent!!)
                    Log.d(contrast.TAG, eventmodel.textevent)
                }
            }
            3 -> {
                //seticonnew
                val eventmodel: Eventmodel = data.getbyIDFake(3)!!
                if (eventmodel.iconevent != 0) {
                    layout_danhiem.setText(eventmodel.textevent)
                    layout_danhiem_img?.setImageResource( eventmodel.iconevent!!)

                    Log.d(contrast.TAG, eventmodel.textevent)
                }
            }
            4 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyIDFake(4)!!
                if (eventmodel.iconevent != 0) {
                    layout_manhinhchinh.setText(eventmodel.textevent)
                    layout_manhinhchinh_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            5 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyIDFake(5)!!
                if (eventmodel.iconevent != 0) {
                    layout_thongbao.setText(eventmodel.textevent)
                    layout_thongbao_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            6 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyIDFake(6)!!
                if (eventmodel.iconevent != 0) {
                    layout_chedoam.setText(eventmodel.textevent)
                    layout_chedoam_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            7 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyIDFake(7)!!
                if (eventmodel.iconevent != 0) {
                    layout_dieukhien.setText(eventmodel.textevent)
                    layout_dieukhien_img?.setImageResource( eventmodel.iconevent!!)

                }
            }
            8 -> {
                //seticonew
                val eventmodel: Eventmodel = data.getbyIDFake(8)!!
                if (eventmodel.iconevent != 0) {
                    layout_khoamanhinh.setText(eventmodel.textevent)
                    layout_khoamanhinh_img?.setImageResource( eventmodel.iconevent!!)

                }
            }

        }
    }

    fun showdialog(idbutton: Int) {
        var dialog: dialogpickerEventnew = dialogpickerEventnew(this, idbutton,this)
        dialog.show()
    }
}
