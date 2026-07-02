package com.theseed.app.data.mapper

import com.theseed.app.data.remote.dto.HabitDto
import com.theseed.app.domain.model.*

fun HabitDto.toDomain(): Habit {
    return Habit(
        id = id,
        title = title,
        category = HabitCategory.valueOf(category.uppercase()),
        difficulty = HabitDifficulty.valueOf(difficulty.uppercase()),
        frequency = HabitFrequency.valueOf(frequency.uppercase()),
        reminderTime = reminderTime?.let {
            // backend sends "HH:mm:ss" — parse just hours/minutes
            java.time.LocalTime.parse(it.substring(0, 5))
        },
        isActive = isActive,
        createdAt = createdAt
    )
}

fun HabitCategory.toApiString() = name.lowercase()
fun HabitDifficulty.toApiString() = name.lowercase()
fun HabitFrequency.toApiString() = name.lowercase()