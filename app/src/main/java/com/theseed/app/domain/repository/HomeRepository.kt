package com.theseed.app.domain.repository

import com.theseed.app.domain.model.HomeDashboard

interface HomeRepository {
    suspend fun getDashboard(): Result<HomeDashboard>
}