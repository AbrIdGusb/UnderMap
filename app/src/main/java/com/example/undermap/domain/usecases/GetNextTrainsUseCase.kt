package com.example.undermap.domain.usecases

import com.example.undermap.data.repos.NextTrainTimeReposImpl
import com.example.undermap.domain.models.GraphCache

class GetNextTrainsUseCase {

    suspend fun setNextTrainTime(id: Int): Pair<Int, Int> {
        val codes = Pair(GraphCache.dataGraph.stationsById[id]!!.codeL, GraphCache.dataGraph.stationsById[id]!!.codeR)
        val repos = NextTrainTimeReposImpl()
        return Pair(repos.getNextTrainTime(codes.first.toInt()), repos.getNextTrainTime(codes.second.toInt()))
    }
}