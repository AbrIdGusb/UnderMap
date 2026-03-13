package com.example.undermap.domain.models

import android.content.Context
import com.example.undermap.data.repos.AlertReposImpl
import com.example.undermap.data.repos.GraphReposImpl
import java.util.Collections.emptyList

object GraphCache {
    var graph: Graph = Graph(emptyList())
    var dataGraph: DataGraph = DataGraph(emptyMap(), emptyMap())

    fun load(context: Context){
        graph = GraphReposImpl().getGraph(context)
        dataGraph = GraphReposImpl().getDataGraph(context)
    }
}

object LiveAlerts {
    var count: Int = 0
    var alerts: List<Alert> = emptyList()

    suspend fun update(){
        val repos = AlertReposImpl()
        alerts = repos.getAlerts(GraphCache.dataGraph)
        count = alerts.size
    }
}