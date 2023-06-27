package com.example.gallery

import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

fun fetchGalleryImages(context: Activity): ArrayList<String>? {
    val galleryImageUrls: ArrayList<String>
    val columns = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID
    ) //get all columns of type images
    val orderBy = MediaStore.Images.Media.DATE_TAKEN //order data by date
    val imagecursor: Cursor = context.managedQuery(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
        null, "$orderBy DESC"
    ) //get all data in Cursor by sorting in DESC order
    galleryImageUrls = ArrayList()
    for (i in 0 until imagecursor.getCount()) {
        imagecursor.moveToPosition(i)
        val dataColumnIndex: Int =
            imagecursor.getColumnIndex(MediaStore.Images.Media.DATA) //get column index
        galleryImageUrls.add(imagecursor.getString(dataColumnIndex)) //get Image from column index
    }
    Log.e("fatch in", "images")
    return galleryImageUrls
}