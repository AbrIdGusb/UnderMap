package com.example.undermap.data.models


data class TouchZone(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
)

data class Label(
    val text: String,
    val x: Float,
    val y: Float
)

data class StationDTO(
    val lineNumber: Int,
    val id: Int,
    val x: Float,
    val y: Float,
    val transitLineNumber: Int,
    val shaderX: Float,
    val shaderY: Float,
    val label: Label,
    val touchZone: TouchZone,
    val codeL: String,
    val codeR: String,
    val transitGroup: Int? = null
)

data class ConnectionDTO (
     val first: StationDTO,
     val second: StationDTO,
     val time: Int,
     val lineNumber: Int,
     val flag: String? = null,
     val curveX: Float? = null,
     val curveY: Float? = null
)

data class LineDTO(
    val number: Int,
    val color: String,
    val stations: List<StationDTO>,
    val connections: List<ConnectionDTO>
)

data class GraphDTO(
    val nodes: List<StationDTO>,
    val edges: List<ConnectionDTO>
)
