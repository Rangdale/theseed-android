package com.theseed.app.domain.usecase.habit

import com.theseed.app.domain.model.HabitCompletion
import com.theseed.app.domain.repository.HabitRepository
import javax.inject.Inject

class ToggleCompletionUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: String): Result<HabitCompletion> {
        return repository.toggleCompletion(habitId)
    }
}