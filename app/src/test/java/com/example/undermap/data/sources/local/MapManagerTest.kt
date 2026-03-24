package com.example.undermap.data.sources.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MapManagerTest {

    private lateinit var context: Context
    private lateinit var mapManager: MapManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        mapManager = MapManager(context)
    }

    // region getGraph() — nodes

    @Test
    fun nodes_notEmpty() {
        Assert.assertTrue(mapManager.getGraph().nodes.isNotEmpty())
    }

    @Test
    fun nodes_uniqueIds() {
        val nodes = mapManager.getGraph().nodes
        Assert.assertEquals(nodes.size, nodes.map { it.id }.toSet().size)
    }

    @Test
    fun nodes_validCoordinates() {
        mapManager.getGraph().nodes.forEach { station ->
            Assert.assertFalse("x is NaN for station ${station.id}", station.x.isNaN())
            Assert.assertFalse("y is NaN for station ${station.id}", station.y.isNaN())
        }
    }

    @Test
    fun nodes_validLineNumber() {
        val validLines = setOf(1, 2, 4, 5, 6)
        mapManager.getGraph().nodes.forEach { station ->
            Assert.assertTrue(
                "station ${station.id} has invalid lineNumber ${station.lineNumber}",
                station.lineNumber in validLines
            )
        }
    }

    @Test
    fun nodes_nonBlankLabel() {
        mapManager.getGraph().nodes.forEach { station ->
            Assert.assertFalse(
                "station ${station.id} has blank label",
                station.label.text.isBlank()
            )
        }
    }

    @Test
    fun nodes_validHitbox() {
        mapManager.getGraph().nodes.forEach { station ->
            val h = station.touchZone
            Assert.assertTrue("station ${station.id}: x1 > x2", h.x1 <= h.x2)
            Assert.assertTrue("station ${station.id}: y1 > y2", h.y1 <= h.y2)
        }
    }

    // endregion

    // region getGraph() — edges

    @Test
    fun edges_notEmpty() {
        Assert.assertTrue(mapManager.getGraph().edges.isNotEmpty())
    }

    @Test
    fun edges_referencesExistingStations() {
        val graph = mapManager.getGraph()
        val stationIds = graph.nodes.map { it.id }.toSet()
        graph.edges.forEach { conn ->
            Assert.assertTrue("unknown fromStation ${conn.first.id}", conn.first.id in stationIds)
            Assert.assertTrue("unknown toStation ${conn.second.id}", conn.second.id in stationIds)
        }
    }

    @Test
    fun edges_positiveTime() {
        mapManager.getGraph().edges.forEach { conn ->
            Assert.assertTrue(
                "connection ${conn.first.id}->${conn.second.id} has non-positive time",
                conn.time > 0
            )
        }
    }

    @Test
    fun edges_flagUHasCurves() {
        mapManager.getGraph().edges.filter { it.flag == "U" }.forEach { conn ->
            Assert.assertNotNull("curveX is null for flag=U", conn.curveX)
            Assert.assertNotNull("curveY is null for flag=U", conn.curveY)
        }
    }

    @Test
    fun edges_noFlagNullCurves() {
        mapManager.getGraph().edges.filter { it.flag == null }.forEach { conn ->
            Assert.assertNull("curveX should be null", conn.curveX)
            Assert.assertNull("curveY should be null", conn.curveY)
        }
    }

    // endregion

    // region getLines()

    @Test
    fun lines_containsExpectedLines() {
        val numbers = mapManager.getLines().map { it.number }.toSet()
        Assert.assertTrue("line 1 missing", 1 in numbers)
        Assert.assertTrue("line 2 missing", 2 in numbers)
    }

    @Test
    fun lines_validHexColors() {
        val hexRegex = Regex("^#[0-9A-Fa-f]{6}$")
        mapManager.getLines().forEach { line ->
            Assert.assertTrue(
                "line ${line.number} has invalid color ${line.color}",
                hexRegex.matches(line.color)
            )
        }
    }

    @Test
    fun lines_eachLineHasStations() {
        mapManager.getLines().forEach { line ->
            Assert.assertTrue("line ${line.number} has no stations", line.stations.isNotEmpty())
        }
    }

    @Test
    fun lines_stationsBelongToCorrectLine() {
        mapManager.getLines().forEach { line ->
            line.stations.forEach { station ->
                Assert.assertEquals(
                    "station ${station.id} has wrong lineNumber in line ${line.number}",
                    line.number,
                    station.lineNumber
                )
            }
        }
    }

    // endregion

    // region Database

    @Test
    fun database_dbFileCopiedToCache() {
        val dbFile = File(context.cacheDir, "mapData.db")
        mapManager.getGraph()
        Assert.assertTrue(dbFile.exists())
    }

    // endregion
}