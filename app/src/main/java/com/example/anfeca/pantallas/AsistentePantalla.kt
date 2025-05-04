@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.anfeca.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.anfeca.R
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class Message(val role: String, val content: String)
data class OpenAIRequest(val model: String = "gpt-3.5-turbo", val messages: List<Message>)
data class OpenAIResponse(val choices: List<Choice>)
data class Choice(val message: Message)

interface OpenAIApi {
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body request: OpenAIRequest,
        @Header("Authorization") auth: String
    ): OpenAIResponse
}

@Composable
fun AsistentePantalla(navController: NavController) {
    val apiKey = "Bearer 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"
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val openAiApi = retrofit.create(OpenAIApi::class.java)

    var mensajes by remember { mutableStateOf(listOf<Message>()) }
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
                    val color = if (mensaje.role == "user") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    Text(
                        text = "${if (mensaje.role == "user") "Tú" else "Asistente"}: ${mensaje.content}",
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
                    val pregunta = Message("user", input)
                    mensajes = mensajes + pregunta
                    input = ""
                    cargando = true

                    scope.launch {
                        try {
                            val response = openAiApi.sendMessage(
                                request = OpenAIRequest(
                                    messages = listOf(
                                        Message("system", "Eres un asistente experto en Lengua de Señas Mexicana."),
                                    ) + mensajes + pregunta
                                ),
                                auth = apiKey
                            )
                            val respuesta = response.choices.firstOrNull()?.message
                            if (respuesta != null) {
                                mensajes = mensajes + respuesta
                            }
                        } catch (e: Exception) {
                            mensajes = mensajes + Message("assistant", "Ocurrió un error: ${e.message}")
                        } finally {
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
