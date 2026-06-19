package com.example.undermap.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.example.undermap.ui.models.MapGraphCache
import com.example.undermap.ui.screens.map.BottomBarView
import com.example.undermap.ui.screens.map.MetroMapScreen
import com.example.undermap.ui.screens.map.StationDialog
import com.example.undermap.ui.theme.AppColors
import com.example.undermap.ui.theme.AppTheme

@Composable
fun App(context: Context) {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                MetroMapScreen(MapGraphCache.mapGraph)
                BottomBarView()
                StationDialog(MapGraphCache.mapGraph.stations.get(20), onDismiss = {})
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(
                        with(LocalDensity.current) {
                            WindowInsets.navigationBars.getBottom(this).toDp()
                        }
                    )
                    .background(AppColors.current.navigationBarColor)
            )
        }
    }
}