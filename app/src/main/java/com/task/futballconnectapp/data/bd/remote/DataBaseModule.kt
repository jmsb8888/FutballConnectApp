package com.task.futballconnectapp.data.bd.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideUserDao(): UserDao {
        return UserDao()
    }
}
