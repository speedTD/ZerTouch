package com.assistive.assistivetouch.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.assistive.assistivetouch.R
import android.content.pm.PackageManager
import android.content.Intent
import android.support.v7.app.ActionBar
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.GridLayoutManager
import com.assistive.assistivetouch.until.contrast
import com.assistive.assistivetouch.adapter.AdapterAppPackage
import com.assistive.assistivetouch.model.packageapp
import kotlinx.android.synthetic.main.activity_pakage_app.*

import android.widget.TextView








class PakageAppActivity : AppCompatActivity() {
     var list:MutableList<packageapp> =ArrayList<packageapp>()
      var adapter:AdapterAppPackage?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pakage_app)
        var actionBar=supportActionBar
        actionBar?.displayOptions=ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.toolbarlayout)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setTitle(getResources()?.getString(R.string.title_changeapp))
        val textviewTitle :TextView = findViewById(R.id.tvTitle)
        textviewTitle.text =getResources()?.getString( R.string.title_changeapp)
        val toolbarbackbtn : AppCompatImageButton = this.findViewById(R.id.toolbar_back)
        toolbarbackbtn.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        ListAllApplication()
        val id=intent.getIntExtra(contrast.PUTIDAPP,1)
        Log.d("thiennu","list "+list.size)
        adapter= AdapterAppPackage(this,list,id)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager=GridLayoutManager(this,4)
        recyclerview.adapter=adapter
        val toolbarsavebtn : AppCompatImageButton = this.findViewById(R.id.toolbar_save)
        toolbarsavebtn.setOnClickListener {
            adapter?.OncompletedChange()
        }

    }
    private fun ListAllApplication():Int  {
        val pm = packageManager
        val main = Intent(Intent.ACTION_MAIN, null)
        main.addCategory(Intent.CATEGORY_LAUNCHER)
        val packages = pm.queryIntentActivities(main, 0)
        val app_name_list = ArrayList<String>()
        val app_package_list = ArrayList<String>()


        for (resolve_info in packages) {
            try {
                val package_name = resolve_info.activityInfo.packageName
                val app_name = pm.getApplicationLabel(
                    pm.getApplicationInfo(package_name, PackageManager.GET_META_DATA)

                ) as String
                var same = false
                for (i in 0 until app_name_list.size) {
                    if (package_name == app_package_list[i])
                        same = true
                }
                if (!same) {
                    list.add(packageapp(app_name, package_name))
                    app_name_list.add(app_name)
                    app_package_list.add(package_name)
                }
                //  Log.i("thiennu", "package = <" + package_name + "> name = <" + app_name + ">");
            } catch (e: Exception) {
            }

        }
        return packages.size
    }


}


