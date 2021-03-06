package com.example.mobileappdev2.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.mobileappdev2.base.BaseView

fun showImagePicker(parent: BaseView, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, "Select Images")
    parent.startActivityForResult(chooser, id)
}

fun readImageFromPath(context: Context, path : String) : Bitmap? {
    var bitmap : Bitmap? = null
    val uri = Uri.parse(path)
    if (uri != null) {
        try {
            val parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.getFileDescriptor()
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
        } catch (e: Exception) {
        }
    }
    return bitmap
}