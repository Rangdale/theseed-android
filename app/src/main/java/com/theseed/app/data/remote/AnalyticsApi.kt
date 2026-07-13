package com.theseed.app.data.remote

import com.theseed.app.data.remote.dto.AnalyticsDashboardDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AnalyticsApi {
    @GET("analytics")
    suspend fun getAnalytics(
        @Header("Authorization") token: String,
        @Query("period") period: String = "weekly"
    ): AnalyticsDashboardDto
}