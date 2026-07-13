package com.theseed.app.domain.repository

import com.theseed.app.domain.model.DisciplineScore
import com.theseed.app.domain.model.ScoreHistoryEntry

interface DisciplineScoreRepository {
    suspend fun calculateAndSave(): Result<DisciplineScore>
    suspend fun getCurrentScore(): Result<DisciplineScore>
    suspend fun getScoreHistory(days: Int = 30): Result<List<ScoreHistoryEntry>>
}