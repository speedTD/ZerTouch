package com.assistive.assistivetouch.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.assistive.assistivetouch.R
import android.widget.ImageView
import com.assistive.assistivetouch.datasave.Database
import com.assistive.assistivetouch.model.Appmodel
import com.assistive.assistivetouch.model.packageapp
import android.app.Activity
import android.content.Intent
import com.assistive.assistivetouch.service.MyTouchService


class AdapterAppPackage(var context: Context? = null, var list2: List<packageapp>? = null, var id: Int) :
    RecyclerView.Adapter<AdapterAppPackage.holder>() {

    var checkIndex: Int = 0

    var data: Database = Database(context)
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): holder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.itemappactivity, p0, false)
        return holder(view)
    }

    override fun getItemCount(): Int {
        return list2?.size!!
    }

    override fun onBindViewHolder(viewHolder: holder, pos: Int) {
        var packageapps = list2?.get(pos)
        viewHolder.nameapp.setText(packageapps?.nameapp)
        val pkg = packageapps?.packagename//your package name
        val icon = context?.getPackageManager()?.getApplicationIcon(pkg)
        if (checkIndex == pos)
            viewHolder.checkapp.visibility = View.VISIBLE
        else
            viewHolder.checkapp.visibility = View.INVISIBLE
        viewHolder.iconapp.setImageDrawable(icon)

        viewHolder.iconapp.setOnClickListener {
            model = Appmodel(pkg, packageapps?.nameapp)
            checkIndex = viewHolder.adapterPosition
            notifyDataSetChanged()
        }

    }

    var model: Appmodel? = null
    fun OncompletedChange() {
        if (!(model == null)) {
            data.updateApp(model!!, id)
            var serviceIntent = Intent(context, MyTouchService::class.java)
            serviceIntent.setAction("com.exemple.assistivetouch.ShowLayoutapp")
            context?.startService(serviceIntent)
            (context as Activity).finish()
        } else {
            val packageapps = list2?.get(0)
            model = Appmodel(packageapps!!.packagename, packageapps.nameapp)
            data.updateApp(model!!, id)
            var serviceIntent = Intent(context, MyTouchService::class.java)
            serviceIntent.setAction("com.exemple.assistivetouch.ShowLayoutapp")
            context?.startService(serviceIntent)
            (context as Activity).finish()
        }
    }

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameapp: TextView = itemView.findViewById(R.id.item_name_app)
        var iconapp: ImageView = itemView.findViewById(R.id.item_app_picture)
        var checkapp: ImageView = itemView.findViewById(R.id.iconcheck)

    }


}