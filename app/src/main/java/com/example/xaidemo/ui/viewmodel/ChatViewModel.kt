package com.example.xaidemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xaidemo.data.mapper.MessageMapper
import com.example.xaidemo.data.model.UiMessage
import com.example.xaidemo.domain.usecase.ClearChatUseCase
import com.example.xaidemo.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<UiMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val inputText: String = ""
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val clearChatUseCase: ClearChatUseCase,
    private val messageMapper: MessageMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputTextChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val messageText = _uiState.value.inputText.trim()
        if (messageText.isEmpty()) return

        // Create user message and convert to UI model
        val userDomainMessage = messageMapper.createUserMessage(messageText)
        val userUiMessage = messageMapper.domainToUi(userDomainMessage)

        _uiState.update { state ->
            state.copy(
                messages = state.messages + userUiMessage,
                inputText = "",
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            sendMessageUseCase(messageText)
                .onSuccess { domainMessage ->
                    // Use Mapper to convert Domain -> UI
                    val assistantUiMessage = messageMapper.domainToUi(domainMessage)
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + assistantUiMessage,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error"
                        )
                    }
                }
        }
    }

    fun clearChat() {
        clearChatUseCase()
        _uiState.update {
            ChatUiState()
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
