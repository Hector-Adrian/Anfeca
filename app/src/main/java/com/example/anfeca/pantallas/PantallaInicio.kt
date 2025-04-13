package com.example.anfeca.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.anfeca.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PantallaInicio(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    val lecciones = listOf(
        Leccion("Fundamentos", "Curso 1", completada = true),
        Leccion("Abecedario", "Curso 1", completada = false),
        Leccion("Saludos", "Curso 1", completada = false)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "¡Bienvenido!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.padding(19.dp)
        )
        ListaLecciones(lecciones = lecciones, navController = navController)
    }
}




data class Leccion(
    val titulo: String,
    val curso: String,
    val completada: Boolean
)

@Composable
fun ListaLecciones(lecciones: List<Leccion>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(lecciones) { leccion ->
            TarjetaLeccion(leccion = leccion, navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



@Composable
fun TarjetaLeccion(leccion: Leccion, navController: NavController) {
    Card(
        onClick = {
            navController.navigate("Leccion/${leccion.titulo}")
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE7B0))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "A continuación",
                color = Color(0xFF1B3A4B),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = leccion.curso,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Lección 1")
                        Text(
                            leccion.titulo,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (leccion.completada) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completado",
                            tint = Color(0xFFFF8000),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}


