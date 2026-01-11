package com.example.xaidemo.data.remote

import com.example.xaidemo.data.model.ChatRequest
import com.example.xaidemo.data.model.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface XAIApiService {

    @Headers("Content-Type: application/json")
    @POST("v1/messages")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): Response<ChatResponse>
}
