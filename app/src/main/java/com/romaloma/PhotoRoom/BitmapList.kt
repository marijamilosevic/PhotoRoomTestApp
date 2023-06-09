package com.romaloma.PhotoRoom

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri

class BitmapList {
     var bitmapList: MutableList<Bitmap> = mutableListOf()

    fun addImageFromUri(imageUri: Uri, contentResolver: ContentResolver) {
        val source = ImageDecoder.createSource(contentResolver, imageUri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        bitmapList.add(bitmap)
    }

    fun replaceImage(pos: Int, bitmap: Bitmap) {
        bitmapList[pos] = bitmap
    }


}