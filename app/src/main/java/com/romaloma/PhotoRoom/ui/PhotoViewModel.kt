package com.romaloma.PhotoRoom.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romaloma.PhotoRoom.api.PhotoRoomApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream


class PhotoViewModel : ViewModel() {
    private val repository = PhotoRoomApi()
    val dataList = MutableLiveData<MutableList<Bitmap?>>(mutableListOf())

    private fun editPhoto(pos: Int, url: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("PhotoViewModel", "edit photo")
            val response = repository.removeBackground(url)
            dataList.value?.set(pos, response)
            viewModelScope.launch(Dispatchers.Main) {
                dataList.notifyObserver()
            }
        }
    }

    fun addPhoto(imageUri: Uri, contentResolver: ContentResolver) {
        //add original bitmap to the list
        viewModelScope.launch(Dispatchers.IO) {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            dataList.value?.add(bitmap)

            viewModelScope.launch (Dispatchers.Main) {
                dataList.notifyObserver()
            }
            //call the remove background API and update the bitmap after receiving the result
            val inputStream = contentResolver.openInputStream(imageUri)
            inputStream?.let { editPhoto(dataList.value?.size?.minus(1) ?: 0, it) }
        }
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}

