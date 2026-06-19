package com.example.undermap.ui.screens.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
                    .height(260.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppColors.current.deepBackColor),
                border = BorderStroke(1.dp, AppColors.current.accentColor)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .shadow(10.dp, RectangleShape)
                            .background(AppColors.current.shallowBackColor)
                            .padding(16.dp, 16.dp, 16.dp, 8.dp),
                    ) {
                        Text(text = station?.label?.text ?: "stat", color = AppColors.current.textLightColor)
                        Text(text = station?.lineNumber.toString(), color = AppColors.current.textWarmColor)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.current.textLightColor,
                                    contentColor = AppColors.current.textDarkColor
                                )
                            ) {
                                Text("From Here")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.current.textLightColor,
                                    contentColor = AppColors.current.textDarkColor
                                )
                            ) {
                                Text("To Here")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(9.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .shadow(10.dp, RectangleShape)
                            .background(AppColors.current.shallowBackColor)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) { } // south
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(34.dp)
                                    .background(AppColors.current.accentColor)
                            ) // divider
                            Column(
                                modifier = Modifier.weight(1f)
                            ) { } // north
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppColors.current.deepBackColor,
                                contentColor = AppColors.current.textLightColor
                            )
                        ) {
                            Text(text = "STATION INFO",)
                        }
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