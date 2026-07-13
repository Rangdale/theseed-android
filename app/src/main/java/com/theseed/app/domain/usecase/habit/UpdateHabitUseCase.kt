package com.theseed.app.domain.usecase.habit

import com.theseed.app.domain.model.Habit
import com.theseed.app.domain.model.HabitCategory
import com.theseed.app.domain.model.HabitDifficulty
import com.theseed.app.domain.model.HabitFrequency
import com.theseed.app.domain.repository.HabitRepository
import javax.inject.Inject

class UpdateHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(
        id: String,
        title: String? = null,
        category: HabitCategory? = null,
        difficulty: HabitDifficulty? = null,
        frequency: HabitFrequency? = null,
        reminderTime: String? = null,
        durationMinutes: Int? = null
    ): Result<Habit> {
        return repository.updateHabit(id, title, category, difficulty, frequency, reminderTime, durationMinutes)
    }
}