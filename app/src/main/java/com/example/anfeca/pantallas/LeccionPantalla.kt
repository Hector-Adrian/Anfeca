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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.anfeca.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class PreguntaLeccion(
    val texto: String = "",
    val opciones: List<String> = emptyList(),
    val respuestaCorrecta: String = ""
)

@Composable
fun LeccionPantalla(navController: NavController, leccionId: String, cursoId: String) {

    val db = Firebase.firestore
    var preguntasLeccion by remember { mutableStateOf<List<PreguntaLeccion>>(emptyList()) }
    var preguntaActualIndex by remember { mutableStateOf(0) }
    var seleccionUsuario by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(true) }

    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    // Cargar preguntas al iniciar
    LaunchedEffect(Unit) {
        db.collection("LeccionesCurso1")
            .document(leccionId)
            .collection("Preguntas${leccionId}") // <- subcolección
            .get()
            .addOnSuccessListener { result ->
                preguntasLeccion = result.documents.mapNotNull { doc ->
                    val texto = doc.getString("texto") ?: return@mapNotNull null
                    val opciones = doc.get("opciones") as? List<*> ?: return@mapNotNull null
                    val respuestaCorrecta = doc.getString("respuestaCorrecta") ?: return@mapNotNull null

                    PreguntaLeccion(
                        texto = texto,
                        opciones = opciones.mapNotNull { it as? String },
                        respuestaCorrecta = respuestaCorrecta
                    )
                }
                cargando = false
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error al cargar subcolección de preguntas", it)
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
                    "¡Lección completada!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (userEmail != null) {
                            // 1. Buscar el nombre de usuario en Firestore usando el email
                            db.collection("Usuarios")
                                .whereEqualTo("email", userEmail)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    val userDoc = querySnapshot.documents.firstOrNull()
                                    val nombreUsuario = userDoc?.id
                                    if (nombreUsuario != null) {
                                        // 2. Actualizar el progreso
                                        db.collection("Usuarios")
                                            .document(nombreUsuario)
                                            .collection("Progreso")
                                            .document("Curso1")
                                            .update(leccionId, true)
                                            .addOnSuccessListener {
                                                navController.navigate("PantallaInicio") {
                                                    popUpTo("PantallaInicio") { inclusive = true }
                                                }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra superior con botón atrás
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text("Lección ${leccionId.takeLast(1)}", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Imagen Placeholder
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Blue, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("IMG", color = Color.White, fontWeight = FontWeight.Bold)
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

        // Opciones
        pregunta.opciones.forEach { opcion ->
            val esCorrecta = opcion == pregunta.respuestaCorrecta
            val color = when (seleccionUsuario) {
                null -> Color.Black
                opcion -> if (esCorrecta) Color.Green else Color.Red
                else -> Color.Black
            }

            val coroutineScope = rememberCoroutineScope()

            Button(
                onClick = {
                    if (seleccionUsuario == null) {
                        seleccionUsuario = opcion
                        // Usar coroutineScope para delay y avanzar
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



