package com.theseed.app.data.repository

import com.theseed.app.data.remote.HomeApi
import com.theseed.app.data.remote.dto.*
import com.theseed.app.domain.model.*
import com.theseed.app.domain.repository.AuthRepository
import com.theseed.app.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi,
    private val authRepository: AuthRepository
) : HomeRepository {

    private suspend fun bearerToken(): String {
        val token = authRepository.getIdToken()
            ?: throw IllegalStateException("No authenticated user")
        return "Bearer $token"
    }

    override suspend fun getDashboard(): Result<HomeDashboard> {
        return try {
            val dto = homeApi.getDashboard(bearerToken())
            Result.success(
                HomeDashboard(
                    displayName = dto.displayName,
                    currentStreak = dto.currentStreak,
                    weekly = dto.weekly.map {
                        WeeklyDay(it.date, it.ratio, it.completed, it.total)
                    },
                    grid = dto.grid.map { GridDay(it.date, it.level) },
                    todayHabits = dto.todayHabits.map {
                        TodayHabit(it.id, it.title, it.category, it.difficulty, it.completedToday)
                    },
                    completedToday = dto.completedToday,
                    totalHabits = dto.totalHabits
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}