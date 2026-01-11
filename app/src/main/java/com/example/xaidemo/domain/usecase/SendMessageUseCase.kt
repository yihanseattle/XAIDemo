package com.example.xaidemo.domain.usecase

import com.example.xaidemo.domain.model.Message
import com.example.xaidemo.domain.repository.IChatRepository
import javax.inject.Inject

/**
 * Send message use case
 * Encapsulates the business logic for sending messages
 */
class SendMessageUseCase @Inject constructor(
    private val chatRepository: IChatRepository
) {
    suspend operator fun invoke(message: String): Result<Message> {
        if (message.isBlank()) {
            return Result.failure(IllegalArgumentException("Message cannot be blank"))
        }
        return chatRepository.sendMessage(message)
    }
}
