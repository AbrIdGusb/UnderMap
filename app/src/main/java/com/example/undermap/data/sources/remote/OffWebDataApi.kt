package com.example.undermap.data.sources.remote

import com.example.undermap.data.models.AlertDTO
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString


class AlertsGetter {

    @Serializable
    data class AlertList(
        val siteWideCustom: List<String>
    )

    val url = "https://alerts.ttc.ca/api/alerts/site-wide"

    suspend fun getAlerts(): List<String> {
        val getter = ByUrlGetter()
        val json = getter.fetchTextFromUrl(url)

        val alertList = Json.decodeFromString<AlertList>(json)
        return alertList.siteWideCustom
    }
}

class NextTrainTimeGetter {

    val baseURL = "https://ntas.ttc.ca/api/ntas/get-next-train-time/"

    suspend fun getNextTrainTime(code: Int): String {
        val url = baseURL + code
        val getter = ByUrlGetter()
        return getter.fetchTextFromUrl(url)
    }
}