package com.theseed.app.domain.model

data class HabitCompletion(
    val habitId: String,
    val completed: Boolean,
    val completionDate: String?  // ← make nullable
)