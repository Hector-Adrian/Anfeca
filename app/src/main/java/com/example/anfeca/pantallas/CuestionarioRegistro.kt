package com.example.anfeca.pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.anfeca.ui.theme.AnfecaTheme

@Composable
fun CuestionarioRegistro(navController: NavController) {
    AnfecaTheme{
        var preguntaIndex by remember { mutableStateOf(0) }
        var nivel by remember { mutableStateOf(0) }

        //Preguntas
        val preguntas = listOf(
            Pregunta(
                titulo = "¿Por qué quieres aprender lengua de señas?",
                subtitulo = "Elige las razones que apliquen",
                opciones = listOf(
                    "Por curiosidad", "Comunicación con mi pareja", "Negocios",
                    "Familiares o amistades", "Viajes", "Escuela", "Otra"
                )
            ),
            Pregunta(
                titulo = "¿Dónde planeas usarla más?",
                subtitulo = "Selecciona tus entornos principales",
                opciones = listOf("Trabajo", "Casa", "Escuela", "Eventos sociales", "Otro")
            ),
            Pregunta(
                titulo = "¿Qué te gustaría ser capaz de hacer en lengua de señas?",
                subtitulo = "Elige tus objetivos",
                opciones = listOf(
                    "Saber más sobre las señas", "Mantener conversaciones",
                    "Comprender a la gente", "Adaptarme a situaciones", "Otro"
                )
            )
        )
        val preguntaNivelIdioma = preguntaIndex == preguntas.lastIndex

        // Estado de selección por opción
        val respuestas = remember { mutableStateMapOf<Int, Set<String>>() }
        var seleccionados by remember { mutableStateOf(setOf<String>()) }

        val preguntaActual = preguntas[preguntaIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = preguntaActual.titulo,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = preguntaActual.subtitulo,
                    fontSize = 14.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                preguntaActual.opciones.forEach { opcion ->
                    OpcionCheck(
                        texto = opcion,
                        seleccionado = seleccionados.contains(opcion),
                        onClick = {
                            seleccionados = if (seleccionados.contains(opcion)) {
                                seleccionados - opcion
                            } else {
                                seleccionados + opcion
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Botón de Siguiente
            Button(
                onClick = {
                    if (preguntaNivelIdioma) {
                        val opcion = seleccionados.firstOrNull()
                        nivel = when (opcion) {
                            "No sé nada" -> 1
                            "Sé lo básico" -> 2
                            "Sé bastante" -> 3
                            else -> 0
                        }
                    } else {
                        respuestas[preguntaIndex] = seleccionados
                        preguntaIndex++
                        seleccionados = emptySet()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF8000),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = if (preguntaNivelIdioma) "Siguiente" else "Finalizar")
            }
        }
    }
}

@Composable
fun OpcionCheck(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(2.dp, Color.White, CircleShape)
                .background(
                    if (seleccionado) Color(0xFFFF8000) else Color.Transparent,
                    shape = CircleShape
                )
        )
    }
}

data class Pregunta(
    val titulo: String,
    val subtitulo: String,
    val opciones: List<String>
)
