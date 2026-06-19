package com.example.undermap.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.lifecycle.ViewModel
import com.example.undermap.domain.usecases.FindRouteUseCase
import com.example.undermap.domain.usecases.GetNextTrainsUseCase
import com.example.undermap.ui.models.CurrentStation
import com.example.undermap.ui.models.MapConnection
import com.example.undermap.ui.models.MapGraphCache
import com.example.undermap.ui.models.MapRoute
import com.example.undermap.ui.models.MapStation
import com.example.undermap.ui.models.PopupData
import com.example.undermap.ui.models.RouteState
import kotlinx.coroutines.flow.MutableStateFlow

class MapViewModel : ViewModel() {

    private val _routeState = mutableStateOf(RouteState(-1, -1))
    val routeState: MutableState<RouteState> = _routeState

    private val _popupData = mutableStateOf(PopupData(false, 0, null))
    val popupData: MutableState<PopupData> = _popupData

    private val _currentStation = mutableStateOf(CurrentStation(-1, true))
    val currentStation: MutableState<CurrentStation> = _currentStation

    suspend fun updateNextTrainTime(){
        val times = _popupData.value.activeStation?.id?.let { GetNextTrainsUseCase().getNextTrainTime(it) }
        _popupData.value.timeLeft = times?.first!!
        _popupData.value.timeRight = times.second
    }

    fun callDialog(station: MapStation){
        //TODO("тут заполняем попап дату, апдейтим ее пока show goes on, и зовем попап")
        _popupData.value = PopupData(true, 1, listOf(station), station)


    }

    fun callRoute(s1: MapStation, s2: MapStation): MapRoute{
        val route = FindRouteUseCase().findRoute(s1.id, s2.id)
        val stations: MutableList<MapStation> = mutableListOf()
        route?.first?.forEach { id ->
            stations.add(MapGraphCache.mapGraph.stations[id]!!)
        }
        val conns: MutableList<MapConnection> = mutableListOf()
        MapGraphCache.mapGraph.connections.forEach { con ->
            if (route?.first?.contains(con.from)!! and route.first.contains(con.to)){
                conns.add(con)
            }
        }
        return MapRoute(stations, conns, route?.second?: 0)
    }

    fun updateTransform(){
        
    }

    fun handleTap(x: Float, y: Float) : MapStation?{
        var tappedStation: MapStation? = null;
        for (s in MapGraphCache.mapGraph.stations.values) {
            if (x - s.x in s.hitbox.x1..s.hitbox.x2
                && y - s.y in s.hitbox.y1..s.hitbox.y2){
                tappedStation = s
                break
            }
        }
        Log.i("tap", tappedStation?.label?.text.toString())
        _currentStation.value = CurrentStation(tappedStation?.id ?: -1, true)
        Log.i("tap", "current station: ${currentStation.value.id}")
        return tappedStation
    }

}