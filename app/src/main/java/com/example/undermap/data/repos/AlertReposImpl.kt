package com.example.undermap.data.repos

import com.example.undermap.data.mappers.AlertMapper
import com.example.undermap.data.sources.remote.AlertsGetter
import com.example.undermap.domain.models.Alert
import com.example.undermap.domain.models.DataGraph
import com.example.undermap.domain.repos.AlertsRepos

class AlertReposImpl : AlertsRepos {

    override suspend fun getAlerts(graph: DataGraph): List<Alert> {
        val alertMapper = AlertMapper()
        val getter = AlertsGetter()

        return alertMapper.mapAlerts(getter.getAlerts(), graph)
    }
}