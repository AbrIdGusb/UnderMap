package com.example.undermap.domain.usecases

import com.example.undermap.domain.algs.dijkstra
import com.example.undermap.domain.models.GraphCache
import com.example.undermap.domain.models.Route

class FindRouteUseCase {

    fun findRoute(start: Int, end: Int): Route? {
        val graph = GraphCache.graph
        if (graph.nodes.isEmpty()) return Route(emptyList(), 0)
        return dijkstra(graph, start, end)
    }
}