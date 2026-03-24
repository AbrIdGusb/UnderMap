package com.example.undermap.ui.models

data class MapRoute(
    val stations: List<MapStation>,
    val connections: List<MapConnection>,
    val timeMins: Int
)