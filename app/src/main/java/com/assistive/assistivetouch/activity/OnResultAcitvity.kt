package com.assistive.assistivetouch.activity

import android.app.Activity
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.assistive.assistivetouch.R
import com.assistive.assistivetouch.datasave.AppPreferences

class OnResultAcitvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_result_acitvity)
        NutKhoaManHinh()

        //finish()
     }
    companion object {
        var devicePolicyManager: DevicePolicyManager?=null
        var com: ComponentName?=null
    }
    fun NutKhoaManHinh() {
        devicePolicyManager = getSystemService(Service.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        com = ComponentName(this, MyAdmin::class.java)
        var isActive = devicePolicyManager!!.isAdminActive(componentName)
        if(isActive){
            finish()
        }else{

        }
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, com)
        intent.putExtra(
            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            getResources().getString(R.string.permistion_deviceadmin))
        startActivityForResult(intent, 111)

    }
    var check=false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==111){
            if (resultCode == Activity.RESULT_OK) {
                check=true
                AppPreferences.isadmin=true
                finish()
                val intent:Intent= Intent(this,MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                //show dialog thông báo bạn chưa cáp quyền ok
            } else {
                finish()
                val intent:Intent= Intent(this,MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
