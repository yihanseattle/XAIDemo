package com.example.xaidemo.data.repository

import com.example.xaidemo.data.mapper.MessageMapper
import com.example.xaidemo.data.model.ChatMessage
import com.example.xaidemo.data.model.ChatRequest
import com.example.xaidemo.data.remote.XAIApiService
import com.example.xaidemo.domain.model.Message
import com.example.xaidemo.domain.repository.IChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ChatRepository implementation
 * Implements the Domain layer interface, handles Data layer logic
 */
@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val apiService: XAIApiService,
    private val messageMapper: MessageMapper
) : IChatRepository {

    private val conversationHistory = mutableListOf<ChatMessage>()

    override suspend fun sendMessage(userMessage: String): Result<Message> = withContext(Dispatchers.IO) {
        try {
            // Add user message to history
            conversationHistory.add(ChatMessage(role = "user", content = userMessage))

            val request = ChatRequest(
                messages = conversationHistory.toList()
            )

            val response = apiService.sendMessage(request)

            if (response.isSuccessful) {
                val chatResponse = response.body()
                if (chatResponse != null) {
                    // Extract assistant message from response and add to history
                    val assistantContent = chatResponse.content?.firstOrNull()?.text ?: ""
                    if (assistantContent.isNotEmpty()) {
                        conversationHistory.add(ChatMessage(role = "assistant", content = assistantContent))
                    }
                    // Use Mapper to convert to Domain layer model
                    val domainMessage = messageMapper.dataToDomain(chatResponse)
                    Result.success(domainMessage)
                } else {
                    conversationHistory.removeLast()
                    Result.failure(Exception("Empty response"))
                }
            } else {
                conversationHistory.removeLast()
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            if (conversationHistory.isNotEmpty()) {
                conversationHistory.removeLast()
            }
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        }
    }

    override fun clearHistory() {
        conversationHistory.clear()
    }
}
