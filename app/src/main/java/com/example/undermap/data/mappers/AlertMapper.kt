package com.example.undermap.data.mappers

import android.content.Context
import com.example.undermap.data.models.AlertDTO
import com.example.undermap.domain.models.Alert
import com.example.undermap.domain.models.DataGraph
import com.example.undermap.domain.models.DataStation
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class AlertMapper {

    fun parseAlert(json: String, graph: DataGraph) : Alert {

        val alertDTO = Json.decodeFromString<AlertDTO>(json)

        val startInst = Instant.parse(alertDTO.activePeriod[1])
        val timeStart = LocalDateTime.ofInstant(startInst, ZoneId.systemDefault())

        val endInst = Instant.parse(alertDTO.activePeriod[0])
        val timeEnd = LocalDateTime.ofInstant(endInst, ZoneId.systemDefault())

        val alert = Alert(
            id = alertDTO.id,
            stopIds = alertDTO.stopIDList.map { s -> graph.stationsByName[s]?.id ?:  -1},
            type = 0,
            activePeriod = Pair(timeStart, timeEnd),
            content = alertDTO.headerText
        )

        return alert
    }


    fun mapAlerts(alertList: List<String>, graph: DataGraph) : List<Alert> {

        val alerts = alertList.map { alert ->
            parseAlert(alert, graph)
        }

        return alerts
    }

}