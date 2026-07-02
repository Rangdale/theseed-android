package com.theseed.app.data.remote

import com.theseed.app.data.remote.dto.*
import retrofit2.http.*

interface HabitApi {
    @POST("habits")
    suspend fun createHabit(
        @Header("Authorization") token: String,
        @Body request: CreateHabitRequest
    ): HabitResponse

    @GET("habits")
    suspend fun getHabits(
        @Header("Authorization") token: String
    ): HabitsResponse

    @GET("habits/{id}")
    suspend fun getHabit(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): HabitResponse

    @PUT("habits/{id}")
    suspend fun updateHabit(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: UpdateHabitRequest
    ): HabitResponse

    @DELETE("habits/{id}")
    suspend fun deleteHabit(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}