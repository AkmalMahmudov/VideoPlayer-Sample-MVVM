package com.akmal.androidtasklessons.di

import com.akmal.androidtasklessons.data.repository.LessonRepository
import com.akmal.androidtasklessons.data.repository.impl.LessonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindOfferRepository(impl: LessonRepositoryImpl): LessonRepository

    fun something2(){

    }
}