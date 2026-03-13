package com.example.undermap.data.mappers

import androidx.compose.ui.graphics.Color
import com.example.undermap.data.models.GraphDTO
import com.example.undermap.data.models.LineDTO
import com.example.undermap.domain.models.Edge
import com.example.undermap.domain.models.Graph
import com.example.undermap.domain.models.Node
import com.example.undermap.ui.models.MapConnection
import com.example.undermap.ui.models.MapGraph
import com.example.undermap.ui.models.MapLine
import com.example.undermap.ui.models.MapStation
import androidx.core.graphics.toColorInt
import com.example.undermap.domain.models.DataGraph
import com.example.undermap.domain.models.DataStation
import com.example.undermap.ui.models.MapHitbox
import com.example.undermap.ui.models.MapLabel
import com.example.undermap.ui.models.MapTransitGroup

class GraphMapper {

    fun mapGraph(graphDTO: GraphDTO): Graph {
        val nodes = mutableMapOf<Int, Node>()
        graphDTO.nodes.forEach {
            nodes[it.id] = (Node(id = it.id, neighbors = emptyList()))
        }
        nodes.forEach { (i, node) ->
            node.neighbors = graphDTO.edges.mapNotNull { edge ->
                when (i) {
                    edge.first.id -> Edge(node, nodes[edge.second.id]!!, edge.time)
                    edge.second.id -> Edge(node, nodes[edge.first.id]!!, edge.time)
                    else -> null
                }
            }
        }
        return Graph(nodes.values.toList())
    }

    fun mapMapGraph(lines: List<LineDTO>, graphDTO: GraphDTO): MapGraph {
        val stations = graphDTO.nodes.associate{ node ->
            node.id to MapStation(
                id = node.id,
                lineNumber = node.lineNumber,
                x = node.x,
                y = node.y,
                shaderX = node.shaderX,
                shaderY = node.shaderY,
                transitLineNumber = node.transitLineNumber,
                alertType = -1,
                hitbox = MapHitbox(node.touchZone.x1, node.touchZone.y1, node.touchZone.x2, node.touchZone.y2),
                label = MapLabel(node.label.text, node.label.x, node.label.y),
                transitGroup = node.transitGroup
            )
        }

        val connections = graphDTO.edges.map { edge ->
            MapConnection(
                from = edge.first.id,
                to = edge.second.id,
                lineNumber = edge.lineNumber,
                flag = edge.flag,
                curveX = edge.curveX,
                curveY = edge.curveY
            )
        }
        val mapLines = lines.associate { line ->
            line.number to MapLine(
                number = line.number,
                color = Color(line.color.toColorInt())
            )
        }

        val transitGroups = graphDTO.nodes
            .filter { it.transitGroup != 0 }
            .groupBy { it.transitGroup!! }
            .mapValues { (groupId, groupNodes) ->
                val groupStations = groupNodes.map { it.id }
                val groupLines = groupNodes.map { it.lineNumber}
                val firstNode = groupNodes.first()

                MapTransitGroup(
                    id = groupId,
                    x = firstNode.x,
                    y = firstNode.y,
                    shaderX = firstNode.shaderX,
                    shaderY = firstNode.shaderY,
                    stations = groupStations,
                    lines = groupLines,
                    label = MapLabel(firstNode.label.text, firstNode.label.x, firstNode.label.y),
                    hitbox = MapHitbox(
                        x1 = groupNodes.minOf { it.touchZone.x1 },
                        y1 = groupNodes.minOf { it.touchZone.y1 },
                        x2 = groupNodes.maxOf { it.touchZone.x2 },
                        y2 = groupNodes.maxOf { it.touchZone.y2 }
                    )
                )
            }

        return MapGraph(
            stations = stations,
            transitGroups = transitGroups,
            connections = connections,
            lines = mapLines
        )
    }

    fun mapDataGraph(graphDTO: GraphDTO): DataGraph {
        val stationsId = graphDTO.nodes.associate { node ->
            node.id to DataStation(
                id = node.id,
                codeL = node.codeL,
                codeR = node.codeR,
                lineNumber = node.lineNumber,
                name = node.label.text

            )
        }
        val stationsName = graphDTO.nodes.associate { node ->
            node.label.text to DataStation(
                id = node.id,
                codeL = node.codeL,
                codeR = node.codeR,
                lineNumber = node.lineNumber,
                name = node.label.text

            )
        }
        return DataGraph(stationsId, stationsName)
    }

}

