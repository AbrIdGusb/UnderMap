package com.example.undermap.domain.repos

import com.example.undermap.domain.models.Alert
import com.example.undermap.domain.models.DataGraph

interface AlertsRepos {

    suspend fun getAlerts(graph: DataGraph): List<Alert>
}