package com.example.undermap.data.repos

import android.content.Context
import com.example.undermap.data.mappers.GraphMapper
import com.example.undermap.data.sources.local.MapManager
import com.example.undermap.domain.models.DataGraph
import com.example.undermap.domain.models.Graph
import com.example.undermap.domain.repos.GraphRepos
import com.example.undermap.ui.models.MapGraph

class GraphReposImpl: GraphRepos {

    override fun getGraph(context: Context): Graph{
        return GraphMapper().mapGraph(MapManager(context).getGraph())

    }

    override fun getMapGraph(context: Context): MapGraph{
        return GraphMapper().mapMapGraph(MapManager(context).getLines(), MapManager(context).getGraph())
    }

    override fun getDataGraph(context: Context): DataGraph{
        return GraphMapper().mapDataGraph(MapManager(context).getGraph())
    }
}