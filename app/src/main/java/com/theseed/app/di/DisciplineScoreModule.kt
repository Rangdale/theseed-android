package com.theseed.app.di

import com.theseed.app.data.repository.DisciplineScoreRepositoryImpl
import com.theseed.app.domain.repository.DisciplineScoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DisciplineScoreModule {
    @Binds
    @Singleton
    abstract fun bindDisciplineScoreRepository(
        impl: DisciplineScoreRepositoryImpl
    ): DisciplineScoreRepository
}