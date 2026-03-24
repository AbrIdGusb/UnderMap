package com.example.undermap.domain.usecases

import com.example.undermap.domain.algs.dijkstra
import com.example.undermap.domain.models.GraphCache
import com.example.undermap.domain.models.Route

class FindRouteUseCase {

    fun findRoute(start: Int, end: Int): Pair<List<Int>, Int>?{
        val graph = GraphCache.graph
        if (graph.nodes.isEmpty()) return Pair(listOf(), 0)
        val r = dijkstra(graph, start, end)
        return Pair(r!!.nodeIds, r.totalTime)
    }
}