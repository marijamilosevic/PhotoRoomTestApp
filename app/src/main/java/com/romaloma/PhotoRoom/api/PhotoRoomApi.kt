package com.romaloma.PhotoRoom.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.http.quote
import io.ktor.utils.io.jvm.javaio.toInputStream
import io.ktor.utils.io.streams.asInput
import java.io.InputStream

class PhotoRoomApi {

    //curl -H 'x-api-key: abc123def456' -f https://sdk.photoroom.com/v1/segment -F 'image_file=@/absolute/path/to/image.jpg' -o /absolute/path/to/result.png

    suspend fun removeBackground(inputStream: InputStream): Bitmap? {

        val client = HttpClient(CIO)

        val formData = formData {
            append(
                "image_file".quote(),
                InputProvider { inputStream.asInput() },
                Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=10000032945")
                }
            )
        }
        Log.d("FORM DATA DONE", "edit photo")
        try {
            val response =
                client.submitFormWithBinaryData(PHOTOROOM_URL, formData) {
                    headers {
                        append("x-api-key", TOKEN_PHOTOROOM)
                    }
                }
            Log.d("Response status", "${response.status}")
            //  val inputStream = response.bodyAsChannel().toInputStream()
            //   var bitmapResult = BitmapFactory.decodeStream(inputStream)
            //  return bitmapResult
        } catch (e: Exception) {
            Log.d("Exception while removing BG: ", "${e.message}")
        }
        client.close()
        Log.d("POSTING IMAGE DONE", "response status")
        return null
    }

    companion object {
        const val TOKEN_PHOTOROOM = "4e1cf2956b116c4015e5086ebd6b653614f4d1c0"
        const val PHOTOROOM_URL = "https://sdk.photoroom.com/v1/segment"
    }

}