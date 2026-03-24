package com.example.undermap.ui.models

import android.content.Context
import com.example.undermap.data.repos.GraphReposImpl
import com.example.undermap.domain.models.DataGraph
import com.example.undermap.domain.models.Graph
import java.util.Collections

object MapGraphCache {
    lateinit var mapGraph: MapGraph

    fun load(context: Context){
        mapGraph = GraphReposImpl().getMapGraph(context)
    }
}