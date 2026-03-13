package com.example.undermap.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.undermap.data.repos.GraphReposImpl
import com.example.undermap.domain.repos.GraphRepos
import com.example.undermap.ui.screens.map.MetroMapScreen
import com.example.undermap.ui.theme.AppTheme

@Composable
fun App(context: Context){
    AppTheme {
        val graphRepos: GraphRepos = GraphReposImpl()
        val graph = remember { graphRepos.getMapGraph(context) }
        MetroMapScreen(graph);
    }

}