package com.example.undermap.ui.viewmodels

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.undermap.domain.models.GraphCache
import com.example.undermap.ui.models.MapGraphCache
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MapViewModelTest {

    private lateinit var viewModel: MapViewModel
    private lateinit var context: Context

    @Before
    fun loadCaches() {
        context = ApplicationProvider.getApplicationContext()
        MapGraphCache.load(context)
        GraphCache.load(context)
    }

    @Before
    fun setUp() {
        viewModel = MapViewModel()
    }

    // ── Sanity check ─────────────────────────────────────────────

    @Test
    fun `graph is loaded and not empty`() {
        assertTrue(MapGraphCache.mapGraph.stations.isNotEmpty())
        assertTrue(MapGraphCache.mapGraph.connections.isNotEmpty())
    }

    // ── callRoute ────────────────────────────────────────────────

    @Test
    fun `callRoute - result contains start and end stations`() {
        val stations = MapGraphCache.mapGraph.stations.values.toList()
        val s1 = stations.first()
        val s2 = stations[stations.size / 2]

        val result = viewModel.callRoute(s1, s2)

        assertTrue(result.stations.contains(s1))
        assertTrue(result.stations.contains(s2))
    }

    @Test
    fun `callRoute - route cost is positive`() {
        val stations = MapGraphCache.mapGraph.stations.values.toList()
        val s1 = stations.first()
        val s2 = stations.last()

        val result = viewModel.callRoute(s1, s2)

        assertTrue(result.timeMins > 0)
    }

    @Test
    fun `callRoute - all connections in result belong to the path`() {
        val stations = MapGraphCache.mapGraph.stations.values.toList()
        val s1 = stations.first()
        val s2 = stations.last()

        val result = viewModel.callRoute(s1, s2)
        val pathIds = result.stations.map { it.id }.toSet()

        result.connections.forEach { conn ->
            assertTrue("conn.from=${conn.from} is not in path", pathIds.contains(conn.from))
            assertTrue("conn.to=${conn.to} is not in path", pathIds.contains(conn.to))
        }
    }

    @Test
    fun `callRoute - route to itself returns single station`() {
        val s1 = MapGraphCache.mapGraph.stations.values.first()

        val result = viewModel.callRoute(s1, s1)

        assertEquals(1, result.stations.size)
        assertTrue(result.connections.isEmpty())
    }

    @Test
    fun `callRoute - adjacent stations are connected by exactly one connection`() {
        val conn = MapGraphCache.mapGraph.connections.first()
        val s1 = MapGraphCache.mapGraph.stations[conn.from]!!
        val s2 = MapGraphCache.mapGraph.stations[conn.to]!!

        val result = viewModel.callRoute(s1, s2)

        assertEquals(2, result.stations.size)
        assertEquals(1, result.connections.size)
        assertEquals(conn, result.connections.first())
    }

    @Test
    fun `callRoute - stations in result match actual objects from graph`() {
        val stations = MapGraphCache.mapGraph.stations.values.toList()
        val s1 = stations.first()
        val s2 = stations.last()

        val result = viewModel.callRoute(s1, s2)

        result.stations.forEach { station ->
            val graphStation = MapGraphCache.mapGraph.stations[station.id]
            assertEquals("Station ${station.id} does not match graph", graphStation, station)
        }
    }
}