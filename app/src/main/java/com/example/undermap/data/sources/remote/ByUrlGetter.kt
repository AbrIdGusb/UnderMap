package com.example.undermap.data.sources.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


class ByUrlGetter {

    suspend fun fetchTextFromUrl(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                response.body?.string() ?: error("Empty body")
            }
        }
    }

}