package com.example.xaidemo.domain.model

/**
 * Domain layer message entity
 * This is the data model used by the core business logic, decoupled from Data and UI layers
 */
data class Message(
    val id: String,
    val content: String,
    val role: MessageRole,
    val timestamp: Long
)

enum class MessageRole {
    USER,
    ASSISTANT
}
