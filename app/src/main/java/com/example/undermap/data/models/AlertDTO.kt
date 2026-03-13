package com.example.undermap.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AlertDTO (
    val id: Int,
    val stopIDList: List<String>,
    val route: Int,
    val activePeriod: List<String>,
    val headerText: String,
    val severity: String,
    val effect: String,
    val direction: String,
)
