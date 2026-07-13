package com.theseed.app.data.remote.dto

data class AnalyticsDashboardDto(
    val period: String,
    val score: Int,
    val scoreDelta: Int,
    val consistencyScore: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val streakStability: Int,
    val completionRate: Int,
    val missedRate: Int,
    val deepWorkHours: Float,
    val bestHabit: BestHabitDto?,
    val peakTimes: List<PeakTimeDto>,
    val trend: List<TrendPointDto>
)

data class BestHabitDto(
    val title: String,
    val completionRate: Int
)

data class PeakTimeDto(
    val label: String,
    val count: Int,
    val ratio: Float
)

data class TrendPointDto(
    val date: String,
    val ratio: Float
)