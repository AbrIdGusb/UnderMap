package com.example.undermap.data.sources.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.undermap.data.models.ConnectionDTO
import com.example.undermap.data.models.*
import com.example.undermap.data.models.Label
import com.example.undermap.data.models.StationDTO
import com.example.undermap.data.models.TouchZone
import java.io.File

class MapManager(private val context: Context) {

    private val dbReg: String = "mapData.db"
    private val dbTwist: String = "mapDataTwisted.db"
    private val db: String = dbTwist

    private fun openDatabase(): SQLiteDatabase {
        val dbFile = File(context.cacheDir, db)

        if (!dbFile.exists()) {
            context.assets.open("subway/$db").use { input ->
                dbFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY)
    }

    fun getGraph(): GraphDTO {
        val db = openDatabase()

        val stations = getStations(db)
        val connections = getConnections(db, stations)

        db.close()
        return GraphDTO(nodes = stations, edges = connections)
    }

    fun getLines(): List<LineDTO> {
        val db = openDatabase()

        val stations = getStations(db)
        val connections = getConnections(db, stations)

        val lines = mutableListOf<LineDTO>()
        db.rawQuery("SELECT number, color FROM lines", null).use { cursor ->
            while (cursor.moveToNext()) {
                val number = cursor.getInt(0)
                val color  = cursor.getString(1)
                lines.add(
                    LineDTO(
                        number      = number,
                        color       = color,
                        stations    = stations.filter { it.lineNumber == number },
                        connections = connections.filter { it.lineNumber == number }
                    )
                )
            }
        }

        db.close()
        return lines
    }

    private fun getStations(db: SQLiteDatabase): List<StationDTO> {
        val result = mutableListOf<StationDTO>()

        val query = """
            SELECT
                s.id, s.name, s.x, s.y, s.codeL, s.codeR, lineNumber, transitGroup,
                sh.color AS transitLineNumber, g.x AS shaderX, g.y AS shaderY,
                l.text  AS labelText,  l.x AS labelX,   l.y AS labelY,
                h.x1, h.y1, h.x2, h.y2
            FROM stops s
            LEFT JOIN shaders  sh ON sh.id = s.id
            LEFT JOIN labels    l ON l.id  = s.id
            LEFT JOIN hitBoxes  h ON h.id  = s.id
            LEFT JOIN "groups"  g ON g.id  = s.transitGroup
        """.trimIndent()

        db.rawQuery(query, null).use { cursor ->
            while (cursor.moveToNext()) {
                result.add(
                    StationDTO(
                        id               = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        lineNumber       = cursor.getInt(cursor.getColumnIndexOrThrow("lineNumber")),
                        x                = cursor.getFloat(cursor.getColumnIndexOrThrow("x")),
                        y                = cursor.getFloat(cursor.getColumnIndexOrThrow("y")),
                        transitLineNumber = cursor.getInt(cursor.getColumnIndexOrThrow("transitLineNumber")),
                        shaderX          = cursor.getFloat(cursor.getColumnIndexOrThrow("shaderX")),
                        shaderY          = cursor.getFloat(cursor.getColumnIndexOrThrow("shaderY")),
                        label = Label(
                            text = cursor.getString(cursor.getColumnIndexOrThrow("labelText")),
                            x    = cursor.getFloat(cursor.getColumnIndexOrThrow("labelX")),
                            y    = cursor.getFloat(cursor.getColumnIndexOrThrow("labelY"))
                        ),
                        touchZone = TouchZone(
                            x1 = cursor.getFloat(cursor.getColumnIndexOrThrow("x1")),
                            y1 = cursor.getFloat(cursor.getColumnIndexOrThrow("y1")),
                            x2 = cursor.getFloat(cursor.getColumnIndexOrThrow("x2")),
                            y2 = cursor.getFloat(cursor.getColumnIndexOrThrow("y2"))
                        ),
                        codeL = cursor.getString(cursor.getColumnIndexOrThrow("codeL")),
                        codeR = cursor.getString(cursor.getColumnIndexOrThrow("codeR")),
                        transitGroup = cursor.getInt(cursor.getColumnIndexOrThrow("transitGroup"))
                    )
                )
            }
        }
        return result
    }

    private fun getConnections(db: SQLiteDatabase, stations: List<StationDTO>): List<ConnectionDTO> {
        val stationMap = stations.associateBy { it.id }
        val result = mutableListOf<ConnectionDTO>()

        val query = """
            SELECT
                c.fromStationId, c.toStationId, c.time, c.lineNumber,
                f.flag, f.curveX, f.curveY
            FROM connections c
            LEFT JOIN flags f ON f.connectionId = c.id
        """.trimIndent()

        db.rawQuery(query, null).use { cursor ->
            while (cursor.moveToNext()) {
                val from = stationMap[cursor.getInt(cursor.getColumnIndexOrThrow("fromStationId"))] ?: return@use
                val to   = stationMap[cursor.getInt(cursor.getColumnIndexOrThrow("toStationId"))]   ?: return@use

                result.add(
                    ConnectionDTO(
                        first      = from,
                        second     = to,
                        time       = cursor.getInt(cursor.getColumnIndexOrThrow("time")),
                        lineNumber = cursor.getInt(cursor.getColumnIndexOrThrow("lineNumber")),
                        flag       = cursor.getString(cursor.getColumnIndexOrThrow("flag")),
                        curveX     = if (cursor.isNull(cursor.getColumnIndexOrThrow("curveX"))) null
                        else cursor.getFloat(cursor.getColumnIndexOrThrow("curveX")),
                        curveY     = if (cursor.isNull(cursor.getColumnIndexOrThrow("curveY"))) null
                        else cursor.getFloat(cursor.getColumnIndexOrThrow("curveY"))
                    )
                )
            }
        }
        return result
    }
}