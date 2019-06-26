package com.assistive.assistivetouch.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.assistive.assistivetouch.R

class SliderAdapter(var context: Context? = null) : PagerAdapter() {
    var layoutInflater:LayoutInflater?=null
    val ImageIntro:IntArray=intArrayOf(R.drawable.ic_intro_1,R.drawable.ic_intro_2,R.drawable.ic_intro_3)
    val HeadingText= arrayOf("Assistive Touch",context?.getResources()?.getString(R.string.Privacy_Policy),context?.getResources()?.getString(R.string.device_admin_permistion))
    val ContentText = arrayOf(
        context?.getResources()?.getString(R.string.content1_intro),
        context?.getResources()?.getString(R.string.content2_intro),
        context?.getResources()?.getString(R.string.content3_intro)
    )
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
     return p0==p1 as RelativeLayout
    }

    override fun getCount(): Int {
        return ImageIntro.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater= context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view:View=layoutInflater!!.inflate(R.layout.sliderlayout,container,false)
        var Image:ImageView=view.findViewById(R.id.intro_image)
        var heading:TextView=view.findViewById(R.id.intro_heading)
        var content:TextView=view.findViewById(R.id.intro_content)
        Image.setImageResource(ImageIntro[position])
        val face = Typeface.createFromAsset(
            context!!.assets,
            "fonts/SanFranciscoDisplay_Bold_1.otf"
        )
        heading.setTypeface(face)
        heading.setText(HeadingText[position])
        content.setText(ContentText[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}