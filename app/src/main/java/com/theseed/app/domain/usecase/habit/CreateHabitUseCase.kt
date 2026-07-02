package com.theseed.app.domain.usecase.habit

import com.theseed.app.domain.model.Habit
import com.theseed.app.domain.model.HabitCategory
import com.theseed.app.domain.model.HabitDifficulty
import com.theseed.app.domain.model.HabitFrequency
import com.theseed.app.domain.repository.HabitRepository
import javax.inject.Inject

class CreateHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(
        title: String,
        category: HabitCategory,
        difficulty: HabitDifficulty,
        frequency: HabitFrequency,
        reminderTime: String?
    ): Result<Habit> {
        // Domain-level validation — same rule as backend, checked early on client too
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("Habit title cannot be empty"))
        }
        return repository.createHabit(title.trim(), category, difficulty, frequency, reminderTime)
    }
}