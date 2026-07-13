package com.theseed.app.data.remote

import com.theseed.app.data.remote.dto.DisciplineScoreDto
import com.theseed.app.data.remote.dto.ScoreHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface DisciplineScoreApi {
    @POST("discipline/calculate")
    suspend fun calculateScore(
        @Header("Authorization") token: String
    ): DisciplineScoreDto

    @GET("discipline/current")
    suspend fun getCurrentScore(
        @Header("Authorization") token: String
    ): DisciplineScoreDto

    @GET("discipline/history")
    suspend fun getScoreHistory(
        @Header("Authorization") token: String,
        @Query("days") days: Int = 30
    ): ScoreHistoryResponse
}