package com.example.xaidemo.data.mapper

import com.example.xaidemo.data.model.ChatMessage
import com.example.xaidemo.data.model.ChatResponse
import com.example.xaidemo.data.model.UiMessage
import com.example.xaidemo.domain.model.Message
import com.example.xaidemo.domain.model.MessageRole
import javax.inject.Inject

/**
 * Message data mapper
 * Responsible for converting data models between different layers
 */
class MessageMapper @Inject constructor() {

    /**
     * Domain Message -> Data ChatMessage (for API requests)
     */
    fun domainToData(message: Message): ChatMessage {
        return ChatMessage(
            role = when (message.role) {
                MessageRole.USER -> "user"
                MessageRole.ASSISTANT -> "assistant"
            },
            content = message.content
        )
    }

    /**
     * Data ChatResponse -> Domain Message
     */
    fun dataToDomain(response: ChatResponse): Message {
        val content = response.content?.firstOrNull()?.text ?: ""
        return Message(
            id = response.id ?: System.currentTimeMillis().toString(),
            content = content,
            role = MessageRole.ASSISTANT,
            timestamp = System.currentTimeMillis()
        )
    }

    /**
     * Domain Message -> UI UiMessage
     */
    fun domainToUi(message: Message): UiMessage {
        return UiMessage(
            id = message.id,
            content = message.content,
            isFromUser = message.role == MessageRole.USER,
            timestamp = message.timestamp
        )
    }

    /**
     * Create user message Domain entity
     */
    fun createUserMessage(content: String): Message {
        return Message(
            id = System.currentTimeMillis().toString(),
            content = content,
            role = MessageRole.USER,
            timestamp = System.currentTimeMillis()
        )
    }

    /**
     * String role -> MessageRole enum
     */
    fun stringToRole(role: String): MessageRole {
        return when (role.lowercase()) {
            "user" -> MessageRole.USER
            "assistant" -> MessageRole.ASSISTANT
            else -> MessageRole.ASSISTANT
        }
    }
}
