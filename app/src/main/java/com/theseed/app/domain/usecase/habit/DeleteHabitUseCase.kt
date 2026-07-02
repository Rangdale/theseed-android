package com.theseed.app.domain.usecase.habit

import com.theseed.app.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteHabit(id)
    }
}