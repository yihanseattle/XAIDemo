package com.example.xaidemo.domain.repository

import com.example.xaidemo.domain.model.Message

/**
 * Domain layer Repository interface
 * Defines the data operation contract needed by business, implementation is in Data layer
 */
interface IChatRepository {
    suspend fun sendMessage(userMessage: String): Result<Message>
    fun clearHistory()
}
