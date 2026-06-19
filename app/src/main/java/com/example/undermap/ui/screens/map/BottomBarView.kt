package com.example.undermap.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.undermap.ui.theme.AppColors
import com.example.undermap.ui.viewmodels.MapViewModel

@Composable
fun BottomBarView(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = viewModel()
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val maxPanelWidth = maxHeight / 2

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 21.dp, vertical = 21.dp)
                .widthIn(max = maxPanelWidth)
                .fillMaxWidth()
                .height(46.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(AppColors.current.shallowBackColor)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Первая кнопка
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(4.dp, 4.dp, 2.dp, 4.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.current.deepBackColor,
                        contentColor = AppColors.current.textColdColor
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart // Выравнивание по левому краю по центру высоты
                    ) {
                        Text(
                            text = "From",
                            modifier = Modifier.padding(start = 0.dp) // Небольшой отступ от самого края кнопки
                        )
                    }
                }
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(2.dp, 4.dp, 4.dp, 4.dp),
                    shape = RoundedCornerShape(9.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.current.deepBackColor,
                        contentColor = AppColors.current.textColdColor
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart // Выравнивание по левому краю по центру высоты
                    ) {
                        Text(
                            text = "To",
                            modifier = Modifier.padding(start = 2.dp) // Небольшой отступ от самого края кнопки
                        )
                    }
                }
            }

        }
    }
}