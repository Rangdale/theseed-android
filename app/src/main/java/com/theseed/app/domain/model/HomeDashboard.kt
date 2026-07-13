package com.theseed.app.domain.model

data class HomeDashboard(
    val displayName: String,
    val currentStreak: Int,
    val weekly: List<WeeklyDay>,
    val grid: List<GridDay>,
    val todayHabits: List<TodayHabit>,
    val completedToday: Int,
    val totalHabits: Int
)

data class WeeklyDay(
    val date: String,
    val ratio: Float,
    val completed: Int,
    val total: Int
)

data class GridDay(
    val date: String,
    val level: Int
)

data class TodayHabit(
    val id: String,
    val title: String,
    val category: String,
    val difficulty: String,
    val completedToday: Boolean
)