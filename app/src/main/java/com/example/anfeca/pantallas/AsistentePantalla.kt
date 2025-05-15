
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

// Modelos de datos actualizados según la API de Cohere
data class CohereChatRequest(
    val message: String,
    val model: String = "command-r-plus",
    val chat_history: List<Map<String, String>> = emptyList(),
    val connectors: List<Map<String, String>> = listOf(mapOf("id" to "web-search")),
    val preamble: String = "Eres un asistente experto en lenguaje de señas. Proporciona respuestas claras y concisas sobre señas, su uso y significado."
)

data class CohereChatResponse(
    val text: String,
    val generation_id: String? = null,
    val chat_history: List<Map<String, String>>? = null
)

interface CohereAPI {
    @POST("v1/chat")
    suspend fun chat(
        @Header("Authorization") auth: String,
        @Body body: CohereChatRequest
    ): CohereChatResponse
}

data class Mensaje(val role: String, val texto: String)

@OptIn(ExperimentalMaterial3Api::class)
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

    var mensajes by remember { mutableStateOf<List<Mensaje>>(emptyList()) }
    var input by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asistente de Lenguaje de Señas") },
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(mensajes) { mensaje ->
                    val color = if (mensaje.role == "USER") Color(0xFFFF8000) else Color(0xFF4CAF50)
                    val alignment = if (mensaje.role == "USER") Alignment.End else Alignment.Start

                    Surface(
                        color = color.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(0.85f)
                            .align(alignment)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (mensaje.role == "USER") "Tú" else "Asistente",
                                style = MaterialTheme.typography.labelMedium,
                                color = color
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = mensaje.texto,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                )
            }

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Escribe tu pregunta sobre lenguaje de señas...") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !cargando
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        val nuevaPregunta = Mensaje("USER", input)
                        mensajes = mensajes + nuevaPregunta
                        cargando = true
                        error = null

                        val historial = mensajes.map { mensaje ->
                            mapOf(
                                "role" to if (mensaje.role == "USER") "USER" else "CHATBOT",
                                "message" to mensaje.texto
                            )
                        }

                        scope.launch {
                            try {
                                val response = cohereApi.chat(
                                    auth = apiKey,
                                    body = CohereChatRequest(
                                        message = input,
                                        chat_history = historial
                                    )
                                )

                                if (response.text.isNotEmpty()) {
                                    val respuesta = Mensaje("CHATBOT", response.text)
                                    mensajes = mensajes + respuesta
                                } else {
                                    error = "Respuesta vacía recibida de Cohere"
                                }
                            } catch (e: Exception) {
                                error = "Error: ${e.message ?: "Desconocido"}"
                                e.printStackTrace()
                            } finally {
                                input = ""
                                cargando = false
                            }
                        }
                    }
                },
                enabled = input.isNotBlank() && !cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }
    }
}