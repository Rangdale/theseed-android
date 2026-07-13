package com.theseed.app.domain.model

data class DisciplineScore(
    val score: Int,
    val consistencyScore: Int,
    val streakStability: Int,
    val completionRate: Int,
    val growthStage: String,
    val growthLevel: Int,
    val scoreDate: String?
)

data class ScoreHistoryEntry(
    val score: Int,
    val consistencyScore: Int,
    val date: String
)