package com.example.undermap.ui.models

import androidx.compose.ui.graphics.Color

data class MapLabel(
    val text: String,
    val x: Float,
    val y: Float
)

data class MapHitbox(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
)

data class MapStation(
    val id: Int,
    val lineNumber: Int,
    val x: Float,
    val y: Float,
    val shaderX: Float? = null,
    val shaderY: Float? = null,
    val transitLineNumber: Int? = null,
    val transitGroup: Int? = null,
    var alertType: Int,
    val label: MapLabel,
    val hitbox: MapHitbox
)

data class MapTransitGroup(
    val id: Int,
    val x: Float,
    val y: Float,
    val shaderX: Float,
    val shaderY: Float,
    val stations: List<Int>,
    val lines: List<Int>,
    val label: MapLabel,
    val hitbox: MapHitbox
)

data class MapConnection(
    val from: Int,
    val to: Int,
    val lineNumber: Int,
    val flag: String? = null,
    val curveX: Float? = null,
    val curveY: Float? = null
)

data class MapLine(
    val number: Int,
    val color: Color
)

data class MapGraph(
    val stations: Map<Int, MapStation>,
    val transitGroups: Map<Int, MapTransitGroup>,
    val connections: List<MapConnection>,
    val lines: Map<Int,MapLine>
)