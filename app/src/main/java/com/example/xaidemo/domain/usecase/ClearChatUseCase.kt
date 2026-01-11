package com.example.xaidemo.domain.usecase

import com.example.xaidemo.domain.repository.IChatRepository
import javax.inject.Inject

/**
 * Clear chat history use case
 */
class ClearChatUseCase @Inject constructor(
    private val chatRepository: IChatRepository
) {
    operator fun invoke() {
        chatRepository.clearHistory()
    }
}
