package com.example.anfeca.datos

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


data class OpenAIRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>
)

data class Message(
    val role: String, // "user" o "assistant"
    val content: String
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

interface OpenAIApi {
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body request: OpenAIRequest,
        @Header("Authorization") auth: String = "Bearer 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"
    ): OpenAIResponse
}



