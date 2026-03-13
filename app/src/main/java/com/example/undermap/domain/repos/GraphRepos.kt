package com.example.undermap.domain.repos

import android.content.Context
import com.example.undermap.domain.models.DataGraph
import com.example.undermap.domain.models.Graph
import com.example.undermap.ui.models.MapGraph

interface GraphRepos {

    fun getGraph(context: Context): Graph

    fun getMapGraph(context: Context): MapGraph

    fun getDataGraph(context: Context): DataGraph
}