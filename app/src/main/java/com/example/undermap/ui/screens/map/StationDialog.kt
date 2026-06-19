package com.example.undermap.ui.screens.map

import android.R.attr.maxHeight
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.undermap.ui.models.MapStation
import com.example.undermap.ui.theme.AppColors

@Composable
fun StationDialog(station: MapStation?, onDismiss: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            val maxPanelWidth = maxHeight / 2

            Card(
                modifier = Modifier
                    .padding(horizontal = 21.dp, vertical = 21.dp)
                    .widthIn(max = maxPanelWidth)
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AppColors.current.deepBackColor)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppColors.current.shallowBackColor)
                    ) {
                        Text(text = station?.label?.text ?: "stat")
                        Text(text = station?.lineNumber.toString())
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = { /*TODO*/ }) { }
                            Button(onClick = { /*TODO*/ }) { }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppColors.current.shallowBackColor)
                    ) {
                        Row {
                            Column(modifier = Modifier.weight(1f)) { } // south
                            Column(modifier = Modifier.width(1.dp)) { } // divider
                            Column(modifier = Modifier.weight(1f)) { } // north
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.fillMaxWidth()
                        ) { }
                    }
                }
            }
        }
    }
}

@Composable
fun NextTrainTimeView(station: MapStation?, dir: Boolean) {
    Column { }
}