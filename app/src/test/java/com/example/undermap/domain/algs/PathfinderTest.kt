package com.example.undermap.domain.algs

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.undermap.domain.models.Graph
import com.example.undermap.domain.models.GraphCache
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PathfinderTest{

    private lateinit var context: Context
    private lateinit var cache: GraphCache

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        cache = GraphCache
        cache.load(context)
    }

    @Test
    fun cache_loads() {
        Assert.assertTrue(cache.graph.nodes.isNotEmpty())
        Assert.assertTrue(cache.dataGraph.stationsById.isNotEmpty())
    }

    @Test
    fun improper_ids(){
        Assert.assertTrue(dijkstra(cache.graph, -1, 1000) == null)
    }

    @Test
    fun same_start_end(){
        val path = dijkstra(cache.graph, 1, 1)
        Assert.assertFalse(path == null)
        Assert.assertTrue(path?.nodeIds?.size == 1)
        Assert.assertTrue(path?.totalTime == 0)
    }

    @Test
    fun same_line(){
        val path = dijkstra(cache.graph, 0, 5)
        Assert.assertFalse(path == null)
        Assert.assertEquals(path?.nodeIds?.size, 6)
        Assert.assertEquals(path?.totalTime, 10)
    }

    @Test
    fun bloor_to_stg(){
        val path = dijkstra(cache.graph, 10, 22)
        Assert.assertFalse(path == null)
        Assert.assertEquals(path?.nodeIds?.size, 5)
        Assert.assertEquals(path?.totalTime, 14)
    }

    @Test
    fun path_contains_valid_node_ids() {
        val path = dijkstra(cache.graph, 0, 5)
        val validIds = cache.graph.nodes.map { it.id }.toSet()
        Assert.assertTrue(path?.nodeIds?.all { it in validIds } == true)
    }

    @Test
    fun path_is_continuous() {
        // каждый следующий узел должен быть соседом предыдущего
        val path = dijkstra(cache.graph, 0, 5)
        Assert.assertNotNull(path)
        val ids = path!!.nodeIds
        for (i in 0 until ids.size - 1) {
            val currentNode = cache.graph.nodes.find { it.id == ids[i] }
            val neighborIds = currentNode?.neighbors?.map { it.endNode.id } ?: emptyList()
            Assert.assertTrue(
                "Node ${ids[i]} is not connected to ${ids[i+1]}",
                ids[i + 1] in neighborIds
            )
        }
    }

    @Test
    fun total_time_matches_edges() {
        // totalTime должен совпадать с суммой весов рёбер по пути
        val path = dijkstra(cache.graph, 0, 5)
        Assert.assertNotNull(path)
        val ids = path!!.nodeIds
        var sum = 0
        for (i in 0 until ids.size - 1) {
            val node = cache.graph.nodes.find { it.id == ids[i] }
            val edge = node?.neighbors?.find { it.endNode.id == ids[i + 1] }
            Assert.assertNotNull("Edge not found between ${ids[i]} and ${ids[i+1]}", edge)
            sum += edge!!.weight
        }
        Assert.assertEquals(sum, path.totalTime)
    }

    @Test
    fun path_starts_and_ends_correctly() {
        val startId = 10
        val endId = 22
        val path = dijkstra(cache.graph, startId, endId)
        Assert.assertNotNull(path)
        Assert.assertEquals(startId, path!!.nodeIds.first())
        Assert.assertEquals(endId, path.nodeIds.last())
    }

    @Test
    fun negative_total_time_impossible() {
        val path = dijkstra(cache.graph, 0, 5)
        Assert.assertTrue((path?.totalTime ?: 0) >= 0)
    }

    @Test
    fun all_node_pairs_reachable_or_null() {
        // дейкстра не должна кидать исключение ни на каких валидных ID
        val ids = cache.graph.nodes.map { it.id }
        for (start in ids.take(5)) {
            for (end in ids.takeLast(5)) {
                Assert.assertFalse(dijkstra(cache.graph, start, end) == null)
            }
        }
    }

    @Test
    fun large_invalid_id_returns_null() {
        Assert.assertNull(dijkstra(cache.graph, Int.MAX_VALUE, 0))
        Assert.assertNull(dijkstra(cache.graph, 0, Int.MAX_VALUE))
    }

    @Test
    fun both_invalid_ids_return_null() {
        Assert.assertNull(dijkstra(cache.graph, -999, -1))
    }
}