package com.theseed.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Header

interface AuthApi {
    @GET("auth/me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): MeResponse
}

data class MeResponse(
    val message: String,
    val user: UserResponse
)

data class UserResponse(
    val uid: String,
    val email: String
)