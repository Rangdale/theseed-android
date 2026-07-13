package com.theseed.app.domain.model

data class AnalyticsDashboard(
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
    val bestHabit: BestHabit?,
    val peakTimes: List<PeakTime>,
    val trend: List<TrendPoint>
)

data class BestHabit(
    val title: String,
    val completionRate: Int
)

data class PeakTime(
    val label: String,
    val count: Int,
    val ratio: Float
)

data class TrendPoint(
    val date: String,
    val ratio: Float
)