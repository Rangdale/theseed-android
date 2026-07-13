package com.theseed.app.presentation.theme

import androidx.compose.ui.graphics.Color
import com.theseed.app.domain.model.HabitDifficulty

val EasyGreen = Color(0xFF4CAF50)
val MediumYellow = Color(0xFFFFC107)
val HardRed = Color(0xFFF44336)

fun HabitDifficulty.color(): Color {
    return when (this) {
        HabitDifficulty.EASY -> EasyGreen
        HabitDifficulty.MEDIUM -> MediumYellow
        HabitDifficulty.HARD -> HardRed
    }
}