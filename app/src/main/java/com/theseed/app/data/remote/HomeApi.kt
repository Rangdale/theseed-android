package com.theseed.app.data.remote

import com.theseed.app.data.remote.dto.HomeDashboardDto
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApi {
    @GET("home")
    suspend fun getDashboard(
        @Header("Authorization") token: String
    ): HomeDashboardDto
}