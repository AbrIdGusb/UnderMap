package com.example.undermap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

        setContent {
            App(this)
        }
    }
}