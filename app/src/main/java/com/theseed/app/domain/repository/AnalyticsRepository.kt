package com.theseed.app.domain.repository

import com.theseed.app.domain.model.AnalyticsDashboard

interface AnalyticsRepository {
    suspend fun getAnalytics(period: String): Result<AnalyticsDashboard>
}