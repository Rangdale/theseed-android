package com.theseed.app.domain.usecase.habit

import com.theseed.app.domain.model.Habit
import com.theseed.app.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(): Result<List<Habit>> {
        return repository.getHabits()
    }
}