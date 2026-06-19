package com.example.undermap

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.example.undermap.domain.models.GraphCache
import com.example.undermap.ui.App
import com.example.undermap.ui.models.MapGraphCache

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GraphCache.load(this)
        MapGraphCache.load(this)
        window.attributes.preferredRefreshRate = 120f

        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false

        setContent {
            App(this)
        }
    }
}