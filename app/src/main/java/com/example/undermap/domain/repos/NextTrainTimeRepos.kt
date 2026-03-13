package com.example.undermap.domain.repos

interface NextTrainTimeRepos {

    suspend fun getNextTrainTime(code: Int) : Int
}