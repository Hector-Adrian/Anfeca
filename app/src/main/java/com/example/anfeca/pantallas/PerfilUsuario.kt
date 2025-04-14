package com.example.anfeca.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.anfeca.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PerfilUsuario(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    var nombre by remember { mutableStateOf("Cargando...") }

    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop, // Ajusta según tu diseño
        modifier = Modifier.fillMaxSize()
    )
    LaunchedEffect(user) {
        user?.email?.let { correo ->
            db.collection("Usuarios")
                .whereEqualTo("correo", correo)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val doc = result.documents.first()
                        nombre = doc.getString("nombre") ?: "Nombre no disponible"
                    } else {
                        nombre = "Usuario no registrado"
                    }
                }
                .addOnFailureListener {
                    nombre = "Error al cargar el nombre"
                }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Text(
                text = "Perfil de Usuario",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }

        item {
            Text("Nombre: $nombre", color = Color.White)
        }

        user?.let {
            item {
                Text("Correo: ${it.email}", color = Color.White)
            }
            item {
                Text("UID: ${it.uid}", color = Color.White)
            }
        } ?: item {
            Text("No hay usuario autenticado", color = Color.White)
        }

        item {
            // Botón naranja: Cerrar sesión
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("inicio_sesion") {
                        popUpTo("PantallaInicio") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8000))
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        }

        item {
            // Botón rojo: Reiniciar progreso
            Button(
                onClick = {
                    user?.uid?.let { uid ->
                        db.collection("Usuarios")
                            .document(uid)
                            .collection("Progreso")
                            .get()
                            .addOnSuccessListener { cursos ->
                                for (curso in cursos) {
                                    db.collection("Usuarios")
                                        .document(uid)
                                        .collection("Progreso")
                                        .document(curso.id)
                                        .set(mapOf<String, Any>()) // Reinicia el documento
                                }
                            }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Reiniciar datos", color = Color.White)
            }
        }
    }
}

