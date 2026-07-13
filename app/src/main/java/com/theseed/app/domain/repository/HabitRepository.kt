package com.theseed.app.domain.repository

import com.theseed.app.domain.model.Habit
import com.theseed.app.domain.model.HabitCategory
import com.theseed.app.domain.model.HabitCompletion
import com.theseed.app.domain.model.HabitDifficulty
import com.theseed.app.domain.model.HabitFrequency

interface HabitRepository {
    suspend fun createHabit(
        title: String,
        category: HabitCategory,
        difficulty: HabitDifficulty,
        frequency: HabitFrequency,
        reminderTime: String?,
        durationMinutes: Int?
    ): Result<Habit>

    suspend fun getHabits(): Result<List<Habit>>

    suspend fun getHabit(id: String): Result<Habit>

    suspend fun updateHabit(
        id: String,
        title: String?,
        category: HabitCategory?,
        difficulty: HabitDifficulty?,
        frequency: HabitFrequency?,
        reminderTime: String?,
        durationMinutes: Int?
    ): Result<Habit>

    suspend fun deleteHabit(id: String): Result<Unit>

    suspend fun toggleCompletion(habitId: String): Result<HabitCompletion>
    suspend fun getTodayCompletions(): Result<Set<String>>
    suspend fun getHabitStreak(habitId: String): Result<Int>
}