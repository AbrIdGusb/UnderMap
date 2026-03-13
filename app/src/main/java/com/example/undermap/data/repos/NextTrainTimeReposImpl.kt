package com.example.undermap.data.repos

import com.example.undermap.data.mappers.TimeParser
import com.example.undermap.data.sources.remote.NextTrainTimeGetter
import com.example.undermap.domain.repos.NextTrainTimeRepos

class NextTrainTimeReposImpl : NextTrainTimeRepos {

    override suspend fun getNextTrainTime(code: Int): Int {

        val timeParser = TimeParser()
        val getter = NextTrainTimeGetter()
        return timeParser.parseNextTrains(getter.getNextTrainTime(code))
    }
}