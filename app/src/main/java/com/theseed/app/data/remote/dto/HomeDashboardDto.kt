package com.theseed.app.data.remote.dto

data class HomeDashboardDto(
    val displayName: String,
    val currentStreak: Int,
    val weekly: List<WeeklyDayDto>,
    val grid: List<GridDayDto>,
    val todayHabits: List<TodayHabitDto>,
    val completedToday: Int,
    val totalHabits: Int
)

data class WeeklyDayDto(
    val date: String,
    val ratio: Float,
    val completed: Int,
    val total: Int
)

data class GridDayDto(
    val date: String,
    val level: Int
)

data class TodayHabitDto(
    val id: String,
    val title: String,
    val category: String,
    val difficulty: String,
    val completedToday: Boolean
)