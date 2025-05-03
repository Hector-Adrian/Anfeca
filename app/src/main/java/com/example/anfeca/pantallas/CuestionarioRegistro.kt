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
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.anfeca.R

@Composable
fun CuestionarioRegistro(navController: NavController) {
    var preguntaIndex by remember { mutableStateOf(0) }
    var nivel by remember { mutableStateOf(0) }


    val respuestas = remember { mutableStateMapOf<Int, Set<String>>() }
    var seleccionados by remember { mutableStateOf(setOf<String>()) }


    var nivelSeleccionado by remember { mutableStateOf("") }

    val preguntaCuestionarios = listOf(
        PreguntaCuestionario(
            titulo = "¿Por qué quieres aprender lengua de señas?",
            subtitulo = "Elige las razones que apliquen",
            opciones = listOf(
                "Por curiosidad", "Comunicación con mi pareja", "Negocios",
                "Familiares o amistades", "Viajes", "Escuela", "Otra"
            )
        ),
        PreguntaCuestionario(
            titulo = "¿Dónde planeas usarla más?",
            subtitulo = "Selecciona tus entornos principales",
            opciones = listOf("Trabajo", "Casa", "Escuela", "Eventos sociales", "Otro")
        ),
        PreguntaCuestionario(
            titulo = "¿Qué te gustaría ser capaz de hacer en lengua de señas?",
            subtitulo = "Elige tus objetivos",
            opciones = listOf(
                "Saber más sobre las señas", "Mantener conversaciones",
                "Comprender a la gente", "Adaptarme a situaciones", "Otro"
            )
        )
    )

    val esUltimaPregunta = preguntaIndex == preguntaCuestionarios.size

    val preguntaCuestionarioActual = if (!esUltimaPregunta) preguntaCuestionarios[preguntaIndex] else
        PreguntaCuestionario(
            titulo = "¿Cuál es tu nivel de lengua de señas?",
            subtitulo = "Selecciona solo una opción",
            opciones = listOf("No sé nada", "Sé lo básico", "Sé bastante")
        )

    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = preguntaCuestionarioActual.titulo,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = preguntaCuestionarioActual.subtitulo,
                fontSize = 14.sp,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(24.dp))

            preguntaCuestionarioActual.opciones.forEach { opcion ->
                if (!esUltimaPregunta) {
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
                } else {
                    OpcionRadio(
                        texto = opcion,
                        seleccionado = nivelSeleccionado == opcion,
                        onClick = { nivelSeleccionado = opcion }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Button(
            onClick = {
                if (!esUltimaPregunta) {
                    respuestas[preguntaIndex] = seleccionados
                    preguntaIndex++
                    seleccionados = emptySet()
                } else {
                    nivel = when (nivelSeleccionado) {
                        "No sé nada" -> 1
                        "Sé lo básico" -> 2
                        "Sé bastante" -> 3
                        else -> 0
                    }

                    navController.navigate("RegistroDatosUsuario")
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
            Text(text = if (esUltimaPregunta) "Finalizar" else "Siguiente")
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

@Composable
fun OpcionRadio(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = seleccionado,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFFF8000),
                unselectedColor = Color.White
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = texto, color = Color.White)
    }
}

data class PreguntaCuestionario(
    val titulo: String,
    val subtitulo: String,
    val opciones: List<String>
)

