package com.assistive.assistivetouch.customdialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.View
import com.assistive.assistivetouch.R

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.assistive.assistivetouch.Interface.ChangeAdminPermision
import com.assistive.assistivetouch.activity.OnResultAcitvity


class dialogpermision(context: Context,changeAdminPermision: ChangeAdminPermision) : Dialog(context) ,View.OnClickListener{
    var content:TextView?=null
    var btnback:Button?=null
    var btnok:Button?=null
    var changeAdminPermision:ChangeAdminPermision?=null
    init
    {
        this.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialogpermission)
        this.changeAdminPermision=changeAdminPermision
        content= findViewById(R.id.content_dialogpermision)
        content!!.setText(context.getResources()?.getString(R.string.noti_request_permission_admin))
        btnback= findViewById(R.id.btn_backdialog)
        btnback!!.setOnClickListener(this)
        btnok=findViewById(R.id.btn_okdialog)
        btnok!!.setOnClickListener(this)
    }
    override fun onClick(v: View?) {

        when(v?.id){
            R.id.btn_backdialog->{
                this.dismiss()
                changeAdminPermision?.ChangerAdmin(false)
            }
            R.id.btn_okdialog->{
                changeAdminPermision?.ChangerAdmin(true)
                val intent: Intent = Intent(context, OnResultAcitvity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }

    override fun dismiss() {
        changeAdminPermision?.ChangerAdmin(false)
        super.dismiss()
    }

    override fun onBackPressed() {
        changeAdminPermision?.ChangerAdmin(false)
    }

}