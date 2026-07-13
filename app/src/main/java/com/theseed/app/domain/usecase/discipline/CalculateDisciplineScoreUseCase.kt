package com.theseed.app.domain.usecase.discipline

import com.theseed.app.domain.model.DisciplineScore
import com.theseed.app.domain.repository.DisciplineScoreRepository
import javax.inject.Inject

class CalculateDisciplineScoreUseCase @Inject constructor(
    private val repository: DisciplineScoreRepository
) {
    suspend operator fun invoke(): Result<DisciplineScore> {
        return repository.calculateAndSave()
    }
}