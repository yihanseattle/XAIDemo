package com.example.xaidemo.di

import com.example.xaidemo.data.repository.ChatRepositoryImpl
import com.example.xaidemo.domain.repository.IChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository binding module
 * Binds interface to concrete implementation
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): IChatRepository
}
