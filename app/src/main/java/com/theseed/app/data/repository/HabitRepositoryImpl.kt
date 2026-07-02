package com.theseed.app.data.repository

import com.theseed.app.data.mapper.toApiString
import com.theseed.app.data.mapper.toDomain
import com.theseed.app.data.remote.HabitApi
import com.theseed.app.data.remote.dto.CreateHabitRequest
import com.theseed.app.data.remote.dto.UpdateHabitRequest
import com.theseed.app.domain.model.*
import com.theseed.app.domain.repository.AuthRepository
import com.theseed.app.domain.repository.HabitRepository
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitApi: HabitApi,
    private val authRepository: AuthRepository
) : HabitRepository {

    private suspend fun bearerToken(): String {
        val token = authRepository.getIdToken()
        android.util.Log.d("TokenDebug", "Token retrieved: ${token?.take(10)}...")
        return if (token != null) "Bearer $token" else throw IllegalStateException("No authenticated user")
    }

    override suspend fun createHabit(
        title: String,
        category: HabitCategory,
        difficulty: HabitDifficulty,
        frequency: HabitFrequency,
        reminderTime: String?
    ): Result<Habit> {
        return try {
            val response = habitApi.createHabit(
                bearerToken(),
                CreateHabitRequest(
                    title = title,
                    category = category.toApiString(),
                    difficulty = difficulty.toApiString(),
                    frequency = frequency.toApiString(),
                    reminderTime = reminderTime
                )
            )
            Result.success(response.habit.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHabits(): Result<List<Habit>> {
        return try {
            val response = habitApi.getHabits(bearerToken())
            Result.success(response.habits.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHabit(id: String): Result<Habit> {
        return try {
            val response = habitApi.getHabit(bearerToken(), id)
            Result.success(response.habit.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateHabit(
        id: String,
        title: String?,
        category: HabitCategory?,
        difficulty: HabitDifficulty?,
        frequency: HabitFrequency?,
        reminderTime: String?
    ): Result<Habit> {
        return try {
            val response = habitApi.updateHabit(
                bearerToken(),
                id,
                UpdateHabitRequest(
                    title = title,
                    category = category?.toApiString(),
                    difficulty = difficulty?.toApiString(),
                    frequency = frequency?.toApiString(),
                    reminderTime = reminderTime
                )
            )
            Result.success(response.habit.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteHabit(id: String): Result<Unit> {
        return try {
            habitApi.deleteHabit(bearerToken(), id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleCompletion(habitId: String): Result<HabitCompletion> {
        return try {
            val response = habitApi.toggleCompletion(bearerToken(), habitId)
            Result.success(
                HabitCompletion(
                    habitId = habitId,
                    completed = response.completed,
                    completionDate = response.completionDate ?: ""  // ← safe fallback
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTodayCompletions(): Result<Set<String>> {
        return try {
            val response = habitApi.getTodayCompletions(bearerToken())
            Result.success(response.completedHabitIds.toSet())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHabitStreak(habitId: String): Result<Int> {
        return try {
            val response = habitApi.getHabitStreak(bearerToken(), habitId)
            Result.success(response.streak)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}