package com.theseed.app.domain.usecase.analytics

import com.theseed.app.domain.model.AnalyticsDashboard
import com.theseed.app.domain.repository.AnalyticsRepository
import javax.inject.Inject

class GetAnalyticsUseCase @Inject constructor(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(period: String = "weekly"): Result<AnalyticsDashboard> {
        return repository.getAnalytics(period)
    }
}