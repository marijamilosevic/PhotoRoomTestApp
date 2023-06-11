package com.romaloma.PhotoRoom.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.quote
import io.ktor.utils.io.jvm.javaio.toInputStream
import io.ktor.utils.io.streams.asInput
import java.io.InputStream

class PhotoRoomApi {

    suspend fun removeBackground(url: InputStream): Bitmap? {
        Log.d("PhotoRoomApi", "Remove background init")
        val client = HttpClient(CIO)
        val formData = formData {
            append("image_file".quote(), InputProvider { url.asInput() }, Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=1000002945")
            })
        }
        Log.d("PhotoRoomApi", "Form data created")
        try {
            val response = client.submitFormWithBinaryData(PHOTOROOM_URL, formData) {
                headers {
                    append("x-api-key", TOKEN_PHOTOROOM)
                }
            }
            Log.d(
                "PhotoRoomApi",
                "Remove background request completed with status ${response.status}"
            )
            val inputStream = response.bodyAsChannel().toInputStream()
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.d("Exception thrown while removing background", "${e.message}")
        } finally {
            client.close()
        }

        Log.d("PhotoRoomApi", "Remove background done")
        return null
    }

    companion object {
        const val TOKEN_PHOTOROOM = "4e1cf2956b116c4015e5086ebd6b653614f4d1c0"
        const val PHOTOROOM_URL = "https://sdk.photoroom.com/v1/segment"
    }

}