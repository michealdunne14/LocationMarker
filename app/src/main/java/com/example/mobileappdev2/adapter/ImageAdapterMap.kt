package com.example.mobileappdev2.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.mobileappdev2.helper.readImageFromPath

class ImageAdapterMap(private val mContext: Context, private val imageList: String): PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getCount(): Int {
        return 1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(mContext)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        if (imageList.contains("https://firebasestorage")) {
            Glide.with(mContext).load(imageList).into(imageView)
        }else{
            imageView.setImageBitmap(readImageFromPath(mContext,imageList))
        }
        container.addView(imageView,0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ImageView)
    }
}