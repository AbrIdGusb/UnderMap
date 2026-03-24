package com.example.undermap.ui.screens.map

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.undermap.ui.models.MapGraph
import com.example.undermap.ui.models.MapLabel
import com.example.undermap.ui.models.MapStation
import com.example.undermap.ui.theme.ColorDark
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.min

@Composable
fun MetroMapScreen(graph: MapGraph) {

    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelMedium // ← вытащи сюда

    val backgroundColor: Color = MaterialTheme.colorScheme.background;
    val stationTextColor: Color = MaterialTheme.colorScheme.onBackground;

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Кешируем текст
    val textLayouts = remember(graph.stations) {
        graph.stations.filter { (_, s) -> s.transitGroup == 0 }.mapValues { (_, station) ->
            textMeasurer.measure(
                text = station.label.text,
                style = labelStyle,
            )
        }
    }

    val groupTextLayouts = remember(graph.transitGroups) {
        graph.transitGroups.mapValues { (_, station) ->
            textMeasurer.measure(
                text = station.label.text,
                style = TextStyle(fontSize = 7.5.sp, color = Color.Black)
            )
        }
    }

    // Кешируем круголечки станций
    val stationPaths = remember(graph.stations) {
        graph.stations.filter { (_, s) -> s.transitGroup == 0 }.mapValues { (_, station) ->
            val r = 5f
            Path().apply {
                addOval(Rect(center = Offset(station.x, station.y), radius = r))
            }
        }
    }

    // Кешируем группы
    val groupPaths = remember(graph.transitGroups) {
        graph.transitGroups.mapValues { (_, group) ->
            val r = 13.3f
            Path().apply {
                addOval(Rect(center = Offset(group.x, group.y), radius = r))
            }
        }
    }

    // Кешируем заливку
    val stationBrushes = remember(graph.stations) {
        graph.stations.mapValues { (_, station) ->
            val stationColor = graph.lines[station.lineNumber]?.color ?: Color.Gray

            SolidColor(stationColor)
        }
    }

    val groupBrushes = remember(graph.transitGroups) {
        graph.transitGroups.mapValues { (_, group) ->
            val center = Offset(group.x, group.y)
            val colors = group.stations.map{ id ->
                graph.lines[graph.stations[id]!!.lineNumber]?.color ?: Color.Gray
            }
            val shiftS = if (graph.stations[group.stations[0]]?.lineNumber == 1){ 0.35f } else { 0.55f }
            val shiftE = if (graph.stations[group.stations[0]]?.lineNumber == 1){ 1.1f } else { 0.55f }

            println(group.id)
            println(shiftE)

            Brush.linearGradient(
                colors = colors,
                start = Offset(
                    center.x - (group.shaderX*11.3f*shiftS),
                    center.y + (group.shaderY*11.3f*shiftS)
                ),
                end = Offset(
                    center.x + (group.shaderX*11.3f*shiftE),
                    center.y - (group.shaderY*11.3f*shiftE)
                )
            )
        }
    }

    val coroutineScope = rememberCoroutineScope()

// 1. Заменяем обычный State на Animatable для offset
    val offsetXAnimatable = remember { Animatable(0f) }
    val offsetYAnimatable = remember { Animatable(0f) }


// 2. Трекер скорости
    val velocityTracker = remember { VelocityTracker() }
    val density = LocalDensity.current
    val minFlingVelocityPx = with(density) { 400.dp.toPx() }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val firstDown = awaitFirstDown(requireUnconsumed = false)

                    // Останавливаем текущую анимацию Fling, если пользователь снова коснулся экрана
                    coroutineScope.launch { offsetXAnimatable.stop(); offsetYAnimatable.stop() }
                    velocityTracker.resetTracking()

                    // Виртуальная позиция для трекера скорости (чтобы избежать скачков при добавлении второго пальца)
                    var panAccumulation = Offset.Zero

                    val contentSizePx = Size(2050f, 3620f)

                    do {
                        val event = awaitPointerEvent()
                        val zoomChange = event.calculateZoom()
                        val panChange = event.calculatePan()
                        val centroid = event.calculateCentroid()

                        if (centroid != Offset.Unspecified) {
                            val newScale = (scale * zoomChange).coerceIn(0.65f, 2f)
                            val scaleChange = newScale / scale

                            val canvasCenter = Offset(size.width / 2f + 50f, size.height / 2f + 250f)

                            // Читаем текущее значение offset из Animatable
                            val currentOffset = Offset(offsetXAnimatable.value, offsetYAnimatable.value)


                            val focalPoint = centroid - canvasCenter - currentOffset

                            val newOffset = currentOffset - focalPoint * (scaleChange - 1f) + panChange

                            val contentWidth = contentSizePx.width * newScale
                            val contentHeight = contentSizePx.height * newScale

                            val maxX = (contentWidth - size.width).coerceAtLeast(0f) / 2f
                            val maxY = (contentHeight - size.height).coerceAtLeast(0f) / 2f

                            // 3. Мгновенно применяем новые координаты (без анимации)
                            coroutineScope.launch {
                                offsetXAnimatable.snapTo(newOffset.x.coerceIn(-maxX, maxX))
                            }
                            coroutineScope.launch {
                                offsetYAnimatable.snapTo(newOffset.y.coerceIn(-maxY, maxY))
                            }
                            scale = newScale

                            // 4. Записываем скорость
                            panAccumulation += panChange
                            velocityTracker.addPosition(
                                timeMillis = event.changes.first().uptimeMillis,
                                position = panAccumulation
                            )
                        }

                        event.changes.forEach { it.consume() }
                    } while (event.changes.any { it.pressed })

                    // --- ЖЕСТ ЗАВЕРШЕН, ЗАПУСКАЕМ FLING ---

                    val velocity = velocityTracker.calculateVelocity()
                    val velocityOffset = Offset(velocity.x, velocity.y)

                    // Пересчитываем финальные границы на основе текущего scale
                    val finalContentWidth = contentSizePx.width * scale
                    val finalContentHeight = contentSizePx.height * scale
                    val maxX = (finalContentWidth - size.width).coerceAtLeast(0f) / 2f
                    val maxY = (finalContentHeight - size.height).coerceAtLeast(0f) / 2f

                    offsetXAnimatable.updateBounds(lowerBound = -maxX, upperBound = maxX)
                    offsetYAnimatable.updateBounds(lowerBound = -maxY, upperBound = maxY)

                    val decay = exponentialDecay<Float>(
                        frictionMultiplier = 1f,
                        absVelocityThreshold = 50.0f
                    )

                    if (velocityOffset.getDistance() > minFlingVelocityPx) {
                        coroutineScope.launch {
                            offsetXAnimatable.animateDecay(velocity.x, decay)
                        }
                        coroutineScope.launch {
                            offsetYAnimatable.animateDecay(velocity.y, decay)
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures{ tapOffset ->
                    var tappedStation: MapStation? = null;
                    for (s in graph.stations.values) {
                        if (tapOffset.x - s.x in s.hitbox.x1..s.hitbox.x2
                            && tapOffset.y - s.y in s.hitbox.y1..s.hitbox.y2){
                            tappedStation = s
                            break
                        }
                    }
                }
            }
    ) {
        withTransform({
            // Используем offsetAnimatable.value
            translate(
                left = size.width / 2f + offsetXAnimatable.value+ 50f,
                top = size.height / 2f + offsetYAnimatable.value + 250f
            )
            scale(scale, scale, pivot = Offset.Zero)
        }) {

            // 1. Соединения
            graph.connections.forEach { conn ->
                val color = graph.lines[conn.lineNumber]?.color ?: Color.Transparent
                var from = Offset(graph.stations[conn.from]!!.x, graph.stations[conn.from]!!.y)
                var to = Offset(graph.stations[conn.to]!!.x, graph.stations[conn.to]!!.y)
                var w = 8f

                if (conn.flag?.contains("Db") ?: false) {
                    from = Offset(from.x, from.y +0f)
                    to = Offset(to.x, to.y +0f)
                }
                if (conn.flag?.contains("Dt") ?: false) {
                    from = Offset(from.x, from.y -7f)
                    to = Offset(to.x, to.y -7f)
                }

                var path: Path = Path().apply {
                    moveTo(from.x, from.y)
                    lineTo(to.x, to.y)
                }

                if (conn.flag?.contains("narrow") ?: false) {
                    w = 7f
                }

                if (conn.flag?.contains("U") ?: false){
                    path = Path().apply {
                        moveTo(from.x, from.y)
                        quadraticTo(from.x + (conn.curveX ?: 0f), from.y + (conn.curveY ?: 0f), to.x, to.y)
                    }
                }

                if (conn.flag?.contains("S") ?: false) {
                    path = Path().apply {
                        moveTo(from.x, from.y)
                        lineTo(from.x + conn.curveX!!, from.y + conn.curveY!!)
                        lineTo(to.x, to.y)
                    }
                }
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(
                        width = w,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.cornerPathEffect(40f)
                    )
                )
                if (conn.flag?.contains("above")?: false) {
                    drawPath(
                        path = path,
                        color = backgroundColor,
                        style = Stroke(
                            width = w/3,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.cornerPathEffect(40f)
                        )
                    )
                }
            }

            // Станции
            graph.stations.filter { (_, s) -> s.transitGroup == 0 }.values.forEach { station ->
                val center = Offset(station.x, station.y)
                val path = stationPaths[station.id] ?: Path()
                val brush = stationBrushes[station.id] ?: SolidColor(Color.Gray)
                val w = 2.7f

                drawCircle(color = backgroundColor, radius = 6f, center = center)
                drawPath(path = path, brush = brush, style = Stroke(width = w))
            }

            fun zeroToOne(v: Float): Int = if (v.toInt() == 0) 1 else 0

            // Группы
            graph.transitGroups.values.forEach { group ->
                val center = Offset(group.x, group.y)
                val path = groupPaths[group.id] ?: Path()
                val brush = groupBrushes[group.id] ?: SolidColor(Color.Gray)
                val w = 4.7f

                println(group.stations)

                drawCircle(color = backgroundColor, radius = 11.3f, center = center)
                drawPath(path = path, brush = brush, style = Stroke(width = w))
            }

            // 3. Подписи
            graph.stations.values.forEach { station ->
                textLayouts[station.id]?.let { layout ->
                    var x = station.x + station.label.x - zeroToOne(station.label.x)*layout.size.width / 2f
                    if (station.label.x < 0) {x -= layout.size.width}
                    var y = station.y + station.label.y - zeroToOne(station.label.y)*layout.size.height / 2f
                    if (station.label.y < 0) {y -= layout.size.height}

                    drawText(
                        textLayoutResult = layout,
                        color = stationTextColor,
                        topLeft = Offset(
                            x = x,
                            y = y
                        )
                    )
                }
            }
            graph.transitGroups.values.forEach { group ->
                groupTextLayouts[group.id]?.let { layout ->
                    var x = group.x + group.label.x - zeroToOne(group.label.x)*layout.size.width / 2f
                    if (group.label.x < 0) {x -= layout.size.width}
                    var y = group.y + group.label.y - zeroToOne(group.label.y)*layout.size.height / 2f
                    if (group.label.y < 0) {y -= layout.size.height}

                    drawText(
                        textLayoutResult = layout,
                        color = stationTextColor,
                        topLeft = Offset(
                            x = x,
                            y = y
                        )
                    )
                }
            }
        }
    }
}