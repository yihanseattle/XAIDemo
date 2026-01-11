package com.example.xaidemo.data.model

import com.google.gson.annotations.SerializedName

/**
 * Chat message data class
 */
data class ChatMessage(
    val role: String,
    val content: String
)

/**
 * Request body sent to xAI API
 */
data class ChatRequest(
    val model: String = "grok-4-0709",
    @SerializedName("max_tokens")
    val maxTokens: Int = 1024,
    val messages: List<ChatMessage>
)

/**
 * Response from xAI API
 */
data class ChatResponse(
    val id: String?,
    val type: String?,
    val role: String?,
    val content: List<ContentBlock>?,
    val model: String?,
    @SerializedName("stop_reason")
    val stopReason: String?,
    @SerializedName("stop_sequence")
    val stopSequence: String?,
    val usage: Usage?
)

data class ContentBlock(
    val type: String?,
    val text: String?
)

data class Usage(
    @SerializedName("input_tokens")
    val inputTokens: Int?,
    @SerializedName("output_tokens")
    val outputTokens: Int?
)

/**
 * UI layer message data class
 */
data class UiMessage(
    val id: String = System.currentTimeMillis().toString(),
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
