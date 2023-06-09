package com.romaloma.PhotoRoom.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romaloma.PhotoRoom.api.PhotoRoomApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.InputStream


class PhotoViewModel(private val repository: PhotoRoomApi) : ViewModel() {
    //    val errorMessage = MutableLiveData<String>()
    val data = MutableLiveData<Bitmap?>()
//    val loading = MutableLiveData<Boolean>()

    fun editPhoto(url: InputStream) {
        viewModelScope.async(Dispatchers.IO) {
            Log.d("PhotoViewModel", "edit photo")
            val response = repository.removeBackground(url)
            data.postValue(response)
        }
    }

    private fun onError(message: String) {
        // errorMessage.value = message
        //  loading.value = false
    }
}