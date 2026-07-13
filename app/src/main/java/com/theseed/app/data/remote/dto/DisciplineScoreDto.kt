package com.theseed.app.data.remote.dto

data class DisciplineScoreDto(
    val score: Int,
    val consistencyScore: Int,
    val streakStability: Int,
    val completionRate: Int,
    val growthStage: String,
    val growthLevel: Int,
    val scoreDate: String?
)

data class ScoreHistoryEntryDto(
    val score: Int,
    val consistencyScore: Int,
    val date: String
)

data class ScoreHistoryResponse(
    val history: List<ScoreHistoryEntryDto>
)