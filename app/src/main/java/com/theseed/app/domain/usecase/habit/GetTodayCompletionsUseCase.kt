package com.theseed.app.domain.usecase.habit

import com.theseed.app.domain.repository.HabitRepository
import javax.inject.Inject

class GetTodayCompletionsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(): Result<Set<String>> {
        return repository.getTodayCompletions()
    }
}