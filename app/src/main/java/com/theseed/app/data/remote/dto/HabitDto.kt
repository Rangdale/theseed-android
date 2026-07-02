package com.theseed.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HabitDto(
    val id: String,
    val title: String,
    val category: String,
    val difficulty: String,
    val frequency: String,
    @SerializedName("reminder_time") val reminderTime: String?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: String
)

data class HabitsResponse(val habits: List<HabitDto>)
data class HabitResponse(val habit: HabitDto)

data class CreateHabitRequest(
    val title: String,
    val category: String,
    val difficulty: String,
    val frequency: String,
    @SerializedName("reminder_time") val reminderTime: String?
)

data class UpdateHabitRequest(
    val title: String? = null,
    val category: String? = null,
    val difficulty: String? = null,
    val frequency: String? = null,
    @SerializedName("reminder_time") val reminderTime: String? = null
)

data class ToggleCompletionResponse(
    val completed: Boolean,
    val completionDate: String?  // ← matches camelCase from backend, no annotation needed
)

data class TodayStatusResponse(
    val completedHabitIds: List<String>
)

data class StreakResponse(
    @SerializedName("habit_id") val habitId: String,
    val streak: Int
)