package com.example.mobileappdev2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.mobileappdev2.helper.readImageFromPath

class ImageAdapter(private val mContext: Context,private val imageList: List<String>): PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(mContext)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        if (imageList[position].contains("https://firebasestorage")) {
            Glide.with(mContext).load(imageList[position]).into(imageView)
        }else{
            imageView.setImageBitmap(readImageFromPath(mContext,imageList[position]))
        }
        container.addView(imageView,0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ImageView)
    }
}