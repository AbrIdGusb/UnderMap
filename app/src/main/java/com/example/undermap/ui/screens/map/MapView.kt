package com.example.undermap.ui.screens.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.undermap.ui.models.MapGraph

@Composable
fun MetroMapScreen(graph: MapGraph) {

    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelMedium // ← вытащи сюда


    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Кешируем текст
    val textLayouts = remember(graph.stations) {
        graph.stations.filter { (_, s) -> s.transitGroup == 0 }.mapValues { (_, station) ->
            textMeasurer.measure(
                text = station.label.text,
                style = labelStyle
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

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)

                    do {
                        val event = awaitPointerEvent()
                        val zoomChange = event.calculateZoom()
                        val panChange = event.calculatePan()
                        val centroid = event.calculateCentroid()

                        if (centroid != Offset.Unspecified) {
                            val newScale = (scale * zoomChange).coerceIn(0.3f, 5f)
                            val scaleChange = newScale / scale

                            val canvasCenter = Offset(size.width / 2f, size.height / 2f)
                            val focalPoint = centroid - canvasCenter - offset

                            offset = offset - focalPoint * (scaleChange - 1f) + panChange
                            scale = newScale
                        }

                        event.changes.forEach { it.consume() }
                    } while (event.changes.any { it.pressed })
                }
            }
    ) {
        withTransform({
            translate(size.width / 2f + offset.x, size.height / 2f + offset.y)
            scale(scale, scale, pivot = Offset.Zero)
        }) {

            // 1. Соединения
            graph.connections.forEach { conn ->
                val color = graph.lines[conn.lineNumber]?.color ?: Color.Gray
                var from = Offset(graph.stations[conn.from]!!.x, graph.stations[conn.from]!!.y)
                var to = Offset(graph.stations[conn.to]!!.x, graph.stations[conn.to]!!.y)
                var w = 8f

                if (conn.flag?.contains("Db") ?: false) {
                    from = Offset(from.x, from.y +4f)
                    to = Offset(to.x, to.y +4f)
                }
                if (conn.flag?.contains("Dt") ?: false) {
                    from = Offset(from.x, from.y -4f)
                    to = Offset(to.x, to.y -4f)
                }

                if (conn.flag?.contains("narrow") ?: false) {
                    w = 5f
                }

                if (conn.flag == "U") {
                    val path = Path().apply {
                        moveTo(from.x, from.y)
                        quadraticTo(from.x + (conn.curveX ?: 0f), from.y + (conn.curveY ?: 0f), to.x, to.y)
                        println(from.x + (conn.curveX ?: 0f))
                        println(from.y + (conn.curveY ?: 0f))
                    }
                    drawPath(
                        path,
                        color = color,
                        style = Stroke(width = w, cap = StrokeCap.Round)
                    )
                } else {
                    if (conn.flag?.contains("S") ?: false) {
                        val path = Path().apply {
                            moveTo(from.x, from.y)
                            lineTo(from.x + conn.curveX!!, from.y + conn.curveY!!)
                            lineTo(to.x, to.y)
                        }
                        drawPath(
                            path,
                            color = color,
                            style = Stroke(
                                width = w,
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.cornerPathEffect(40f)
                            )
                        )
                    } else {
                        if (conn.flag == "above") {
                            drawLine(
                                color = color,
                                start = from,
                                end = to,
                                strokeWidth = w,
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = Color.White,
                                start = from,
                                end = to,
                                strokeWidth = w / 3,
                                cap = StrokeCap.Round
                            )
                        } else {
                            drawLine(
                                color = color,
                                start = from,
                                end = to,
                                strokeWidth = w,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }

            // Станции
            graph.stations.filter { (_, s) -> s.transitGroup == 0 }.values.forEach { station ->
                val center = Offset(station.x, station.y)
                val path = stationPaths[station.id] ?: Path()
                val brush = stationBrushes[station.id] ?: SolidColor(Color.Gray)
                val w = 2.7f

                drawCircle(color = Color.White, radius = 6f, center = center)
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

                drawCircle(color = Color.White, radius = 11.3f, center = center)
                drawPath(path = path, brush = brush, style = Stroke(width = w))
            }

            // 3. Подписи
            graph.stations.values.forEach { station ->
                textLayouts[station.id]?.let { layout ->
                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(
                            x = station.x + station.label.x - zeroToOne(station.label.x)*layout.size.width / 2f,
                            y = station.y + station.label.y - zeroToOne(station.label.y)*layout.size.height / 2f
                        )
                    )
                }
            }
            graph.transitGroups.values.forEach { group ->
                groupTextLayouts[group.id]?.let { layout ->
                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(
                            x = group.x + group.label.x - zeroToOne(group.label.x)*layout.size.width / 2f,
                            y = group.y + group.label.y - zeroToOne(group.label.y)*layout.size.height / 2f
                        )
                    )
                }
            }
        }
    }
}