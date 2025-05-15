@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.anfeca.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class CohereChatRequest(val message: String, val model: String = "command-r-plus", val chat_history: List<CohereMessage> = emptyList())

data class CohereMessage(val role: String, val message: String)

data class CohereChatResponse(val text: String)

interface CohereAPI {
    @POST("v1/chat")
    suspend fun chat(
        @Header("Authorization") auth: String,
        @Body body: CohereChatRequest
    ): CohereChatResponse
}

@Composable
fun AsistentePantalla(navController: NavController) {
    val apiKey = "Bearer NVa1fx9L2OkSuTdc4W0EFvQD4oIjwptnwLA9y8E4"
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://api.cohere.ai/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val cohereApi = retrofit.create(CohereAPI::class.java)

    var mensajes by remember { mutableStateOf<List<CohereMessage>>(emptyList()) }
    var input by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var cargando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asistente Virtual") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(mensajes) { mensaje ->
                    val color = if (mensaje.role == "USER") Color(0xFFFF8000) else Color.White
                    Text(
                        text = "${if (mensaje.role == "USER") "Tú" else "Asistente"}: ${mensaje.message}",
                        color = color,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Escribe tu pregunta...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val nuevaPregunta = CohereMessage("USER", input)
                    mensajes = mensajes + nuevaPregunta
                    cargando = true
                    val historial = mensajes

                    scope.launch {
                        try {
                            val response = cohereApi.chat(
                                auth = apiKey,
                                body = CohereChatRequest(
                                    message = input,
                                    chat_history = historial
                                )
                            )
                            val respuesta = CohereMessage("CHATBOT", response.text)
                            mensajes = mensajes + respuesta
                        } catch (e: Exception) {
                            mensajes = mensajes + CohereMessage("CHATBOT", "Error: ${e.message}")
                        } finally {
                            input = ""
                            cargando = false
                        }
                    }
                },
                enabled = input.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }
    }
}

