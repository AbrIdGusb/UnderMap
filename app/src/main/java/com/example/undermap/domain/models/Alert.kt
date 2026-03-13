package com.example.undermap.domain.models

import java.time.LocalDateTime

data class Alert (
    val id: Int,
    val stopIds: List<Int>,
    val type: Int,
    val activePeriod: Pair<LocalDateTime, LocalDateTime>,
    val content: String
)