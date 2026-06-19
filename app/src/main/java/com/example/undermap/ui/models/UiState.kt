package com.example.undermap.ui.models

import android.R

data class RouteState(
    val firstSelectedId: Int,
    val secondSelectedId: Int,
)

data class PopupData(
    val isActive: Boolean,
    val numStations: Int = 0,
    val stations: List<MapStation?>?,
    val activeStation: MapStation? = stations?.first(),
    var timeLeft: Int = -1,
    var timeRight: Int = -1,
    val lineName: String = " ",
)

data class CurrentStation(
    var id: Int,
    val isFirst: Boolean
)