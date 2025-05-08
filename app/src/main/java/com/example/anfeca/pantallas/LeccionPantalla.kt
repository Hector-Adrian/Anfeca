package com.example.anfeca.pantallas


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.anfeca.R
import com.example.anfeca.ui.theme.Naranja
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class PreguntaLeccion(
    val texto: String = "",
    val opciones: List<String> = emptyList(),
    val respuestaCorrecta: String = "",
    val urlImagen: String = ""
)

@Composable
fun LeccionPantalla(navController: NavController, leccionId: String, cursoId: String) {

    val db = Firebase.firestore
    var preguntasLeccion by remember { mutableStateOf<List<PreguntaLeccion>>(emptyList()) }
    var preguntaActualIndex by remember { mutableStateOf(0) }
    var seleccionUsuario by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var respuestasCorrectas by remember { mutableStateOf(0) }
    val total = preguntasLeccion.size
    val porcentaje = (respuestasCorrectas.toFloat() / total) * 100
    val aprobo = porcentaje >= 70

    // Cargar preguntas al iniciar
    LaunchedEffect(Unit) {
        db.collection("Lecciones$cursoId")
            .document(leccionId)
            .collection("Preguntas$leccionId")
            .get()
            .addOnSuccessListener { result ->
                preguntasLeccion = result.documents.mapNotNull { doc ->
                    val texto = doc.getString("texto") ?: return@mapNotNull null
                    val opciones = doc.get("opciones") as? List<*> ?: return@mapNotNull null
                    val respuestaCorrecta = doc.getString("respuestaCorrecta") ?: return@mapNotNull null
                    val urlImagen = doc.getString("urlImagen") ?: ""

                    PreguntaLeccion(
                        texto = texto,
                        opciones = opciones.mapNotNull { it as? String },
                        respuestaCorrecta = respuestaCorrecta,
                        urlImagen = urlImagen
                    )
                }
                cargando = false
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error al cargar preguntas", it)
                cargando = false
            }
    }


    if (cargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (preguntaActualIndex >= preguntasLeccion.size) {
        // Final de la lección con botón para regresar
        val auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val userEmail = auth.currentUser?.email

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    if (aprobo) "¡Buen trabajo! Has completado la lección."
                    else "Intenta nuevamente para completar la lección.",
                    color = if (aprobo) Naranja else Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val total = preguntasLeccion.size
                        val porcentaje = (respuestasCorrectas.toFloat() / total) * 100

                        if (userEmail != null) {
                            db.collection("Usuarios")
                                .whereEqualTo("email", userEmail)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    val userDoc = querySnapshot.documents.firstOrNull()
                                    val nombreUsuario = userDoc?.id
                                    if (nombreUsuario != null) {
                                        if (porcentaje >= 70) {
                                            db.collection("Usuarios")
                                                .document(nombreUsuario)
                                                .collection("Progreso")
                                                .document("Curso1")
                                                .update(leccionId, true)
                                        }
                                        navController.navigate("PantallaInicio") {
                                            popUpTo("PantallaInicio") { inclusive = true }
                                        }
                                    }
                                }
                        } else {
                            navController.navigate("PantallaInicio") {
                                popUpTo("PantallaInicio") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Volver al inicio")
                }

            }
        }
        return
    }




    val pregunta = preguntasLeccion[preguntaActualIndex]
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.registro_se),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra superior con botón atrás
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White
                    )
                }
                Text("Lección ${leccionId.takeLast(1)}", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (pregunta.urlImagen.isNotBlank()) {
                // Mostrar imagen desde URL si existe
                Image(
                    painter = rememberAsyncImagePainter(model = pregunta.urlImagen),
                    contentDescription = "Imagen de seña",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                // Placeholder si no hay imagen
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.Blue, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("IMG", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pregunta
            Text(
                pregunta.texto,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones con opciones
            pregunta.opciones.forEach { opcion ->
                val esCorrecta = opcion == pregunta.respuestaCorrecta
                val color = when (seleccionUsuario) {
                    null -> Color.DarkGray
                    opcion -> if (esCorrecta) Color.Green else Color.Red
                    else -> Color.DarkGray
                }

                val coroutineScope = rememberCoroutineScope()

                Button(
                    onClick = {
                        if (seleccionUsuario == null) {
                            seleccionUsuario = opcion
                            if (opcion == pregunta.respuestaCorrecta) {
                                respuestasCorrectas++
                            }

                            coroutineScope.launch {
                                delay(1000)
                                seleccionUsuario = null
                                preguntaActualIndex++
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = color),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(opcion)
                }

            }
        }
    }
}



