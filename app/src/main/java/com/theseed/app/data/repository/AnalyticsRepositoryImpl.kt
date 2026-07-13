package com.theseed.app.data.repository

import com.theseed.app.data.remote.AnalyticsApi
import com.theseed.app.domain.model.*
import com.theseed.app.domain.repository.AnalyticsRepository
import com.theseed.app.domain.repository.AuthRepository
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val api: AnalyticsApi,
    private val authRepository: AuthRepository
) : AnalyticsRepository {

    private suspend fun bearerToken(): String {
        val token = authRepository.getIdToken()
            ?: throw IllegalStateException("No authenticated user")
        return "Bearer $token"
    }

    override suspend fun getAnalytics(period: String): Result<AnalyticsDashboard> {
        return try {
            val dto = api.getAnalytics(bearerToken(), period)
            Result.success(
                AnalyticsDashboard(
                    period = dto.period,
                    score = dto.score,
                    scoreDelta = dto.scoreDelta,
                    consistencyScore = dto.consistencyScore,
                    currentStreak = dto.currentStreak,
                    longestStreak = dto.longestStreak,
                    streakStability = dto.streakStability,
                    completionRate = dto.completionRate,
                    missedRate = dto.missedRate,
                    deepWorkHours = dto.deepWorkHours,
                    bestHabit = dto.bestHabit?.let {
                        BestHabit(it.title, it.completionRate)
                    },
                    peakTimes = dto.peakTimes.map {
                        PeakTime(it.label, it.count, it.ratio)
                    },
                    trend = dto.trend.map {
                        TrendPoint(it.date, it.ratio)
                    }
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}