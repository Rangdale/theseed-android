package com.theseed.app.data.repository

import com.theseed.app.data.remote.DisciplineScoreApi
import com.theseed.app.domain.model.DisciplineScore
import com.theseed.app.domain.model.ScoreHistoryEntry
import com.theseed.app.domain.repository.AuthRepository
import com.theseed.app.domain.repository.DisciplineScoreRepository
import javax.inject.Inject

class DisciplineScoreRepositoryImpl @Inject constructor(
    private val api: DisciplineScoreApi,
    private val authRepository: AuthRepository
) : DisciplineScoreRepository {

    private suspend fun bearerToken(): String {
        val token = authRepository.getIdToken()
            ?: throw IllegalStateException("No authenticated user")
        return "Bearer $token"
    }

    private fun mapDto(dto: com.theseed.app.data.remote.dto.DisciplineScoreDto) = DisciplineScore(
        score = dto.score,
        consistencyScore = dto.consistencyScore,
        streakStability = dto.streakStability,
        completionRate = dto.completionRate,
        growthStage = dto.growthStage,
        growthLevel = dto.growthLevel,
        scoreDate = dto.scoreDate
    )

    override suspend fun calculateAndSave(): Result<DisciplineScore> {
        return try {
            val dto = api.calculateScore(bearerToken())
            Result.success(mapDto(dto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentScore(): Result<DisciplineScore> {
        return try {
            val dto = api.getCurrentScore(bearerToken())
            Result.success(mapDto(dto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getScoreHistory(days: Int): Result<List<ScoreHistoryEntry>> {
        return try {
            val response = api.getScoreHistory(bearerToken(), days)
            Result.success(response.history.map {
                ScoreHistoryEntry(
                    score = it.score,
                    consistencyScore = it.consistencyScore,
                    date = it.date
                )
            })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}