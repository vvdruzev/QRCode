package com.example.qrcode

import android.app.DownloadManager
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream


class NetworkClient {
    fun get(url:String): InputStream {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id","3")
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        val body = response.body()
        return body!!.byteStream()
    }
}