package com.theseed.app.domain.repository

import com.theseed.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Returns current user or null if not logged in
    fun getCurrentUser(): User?
    // Emits auth state changes in real time
    fun getAuthState(): Flow<User?>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut()
    suspend fun getIdToken(): String?
}