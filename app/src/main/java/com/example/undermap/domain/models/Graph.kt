package com.example.undermap.domain.models

data class Edge(
    val startNode: Node,
    val endNode: Node,
    val weight: Int
)

data class Node(
    val id: Int,
    var neighbors: List<Edge>
)

data class Graph(
    val nodes: List<Node>
)

data class DataStation(
    val id: Int,
    val codeL: String,
    val codeR: String,
    val lineNumber: Int,
    val name: String
)

data class DataGraph(
    val stationsById: Map<Int, DataStation>,
    val stationsByName: Map<String, DataStation>,
)

data class Route(
    val nodeIds: List<Int>,
    val totalTime: Int
)