package com.example.undermap.data.mappers

import kotlin.collections.map
import org.json.JSONArray


class TimeParser {

    fun parseNextTrains(json: String): Int {
        val jsonArray = JSONArray(json)
        val firstObject = jsonArray.getJSONObject(0)
        val nextTrainsString = firstObject.getString("nextTrains")

        return nextTrainsString
            .split(",")
            .map { it.trim().toInt() }[0]
    }
}