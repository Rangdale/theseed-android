package com.theseed.app.domain.model

import java.time.LocalTime

enum class HabitCategory {
    WELLNESS, PRODUCTIVITY, FITNESS, MINDFULNESS, LEARNING, NUTRITION, SOCIAL, DEEP_WORK, OTHER
}

enum class HabitDifficulty {
    EASY, MEDIUM, HARD
}

enum class HabitFrequency {
    DAILY, WEEKLY
}

data class Habit(
    val id: String,
    val title: String,
    val category: HabitCategory,
    val difficulty: HabitDifficulty,
    val frequency: HabitFrequency,
    val reminderTime: LocalTime?,
    val isActive: Boolean,
    val createdAt: String,
    val durationMinutes: Int?
)

fun HabitCategory.displayName(): String {
    return when (this) {
        HabitCategory.DEEP_WORK -> "Deep Work"
        else -> name.lowercase().replaceFirstChar { it.uppercase() }
    }
}