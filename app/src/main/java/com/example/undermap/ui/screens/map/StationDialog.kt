package com.example.undermap.ui.screens.map

import android.app.Dialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.undermap.ui.models.MapStation

@Composable
fun StationDialog(station: MapStation, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        // Основной контейнер окна
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2E31)) // Темный фон
        ) {
            Column() {

            }

            Column() {

            }
        }
    }
}