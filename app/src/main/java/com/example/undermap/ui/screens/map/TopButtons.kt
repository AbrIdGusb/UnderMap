package com.example.undermap.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.undermap.ui.theme.AppColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding


@Composable
fun TopButtonsView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Левая круглая кнопка (radius 16dp = 32dp diameter)
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(AppColors.current.shallowBackColor)
        ) {
//            Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = "Menu",
//                tint = AppColors.current.textLightColor
//            )
        }

        // Правый блок из 2 кнопок (32x32dp каждая, radius 4dp)
        Column {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(AppColors.current.shallowBackColor)
            ) {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search",
//                    tint = AppColors.current.textLightColor
//                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(AppColors.current.shallowBackColor)
            ) {
//                Icon(
//                    imageVector = Icons.Default.Settings,
//                    contentDescription = "Settings",
//                    tint = AppColors.current.textDarkColor
//                )
            }
        }
    }
}