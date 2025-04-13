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
    val context = LocalContext.current
    var lecciones by remember { mutableStateOf<List<Leccion>>(emptyList()) }

    val user = FirebaseAuth.getInstance().currentUser
    val nombreUsuario = user?.displayName ?: "NombrePorDefecto"

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Usuarios").document(nombreUsuario)
            .collection("Progreso").document("Curso1")

        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val progreso = document.data ?: emptyMap<String, Any>()
                lecciones = listOf(
                    Leccion("Fundamentos", "Curso 1", progreso["Leccion1"] as? Boolean ?: false),
                    Leccion("Abecedario", "Curso 1", progreso["Leccion2"] as? Boolean ?: false),
                    Leccion("Saludos", "Curso 1", progreso["Leccion3"] as? Boolean ?: false)
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.registro_se),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column {
            Text(
                text = "Bienvenido, $nombreUsuario",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            ListaLecciones(lecciones)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ir al Plan de Estudio", color = Color.White)
                Switch(
                    checked = false,
                    onCheckedChange = { navController.navigate("PlanEstudio") },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFF8000))
                )
            }
        }
    }
}



data class Leccion(
    val titulo: String,
    val curso: String,
    val completada: Boolean
)

@Composable
fun ListaLecciones(lecciones: List<Leccion>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDE7B0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Curso 1",
                color = Color(0xFF1B3A4B),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            lecciones.forEachIndexed { index, leccion ->
                TarjetaLeccion(leccion, index + 1)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Composable
fun TarjetaLeccion(leccion: Leccion, numero: Int) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Lección $numero", fontWeight = FontWeight.Bold)
                    Text(leccion.titulo)
                }
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

