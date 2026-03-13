package com.example.undermap.domain.algs

import com.example.undermap.domain.models.Graph
import com.example.undermap.domain.models.Node
import com.example.undermap.domain.models.Route
import java.util.PriorityQueue



fun dijkstra(graph: Graph, startId: Int, endId: Int): Route? {
    val dist = mutableMapOf<Int, Int>()
    val prev = mutableMapOf<Int, Int?>()
    val visited = mutableSetOf<Int>()

    graph.nodes.forEach { dist[it.id] = Int.MAX_VALUE }
    dist[startId] = 0

    val queue = ArrayDeque<Int>()
    queue.add(startId)

    while (queue.isNotEmpty()) {
        val current = queue.minByOrNull { dist[it] ?: Int.MAX_VALUE } ?: break
        queue.remove(current)

        if (current in visited) continue
        visited.add(current)

        if (current == endId) break

        val node = graph.nodes.find { it.id == current } ?: continue

        for (edge in node.neighbors) {
            val neighbor = edge.endNode.id
            if (neighbor in visited) continue

            val newDist = (dist[current] ?: Int.MAX_VALUE) + edge.weight
            if (newDist < (dist[neighbor] ?: Int.MAX_VALUE)) {
                dist[neighbor] = newDist
                prev[neighbor] = current
                queue.add(neighbor)
            }
        }
    }

    val totalTime = dist[endId] ?: return null
    if (totalTime == Int.MAX_VALUE) return null

    val path = mutableListOf<Int>()
    var current: Int? = endId
    while (current != null) {
        path.add(current)
        current = prev[current]
    }
    path.reverse()

    return Route(nodeIds = path, totalTime = totalTime)
}