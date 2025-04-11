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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.anfeca.R

@Composable
fun PantallaInicio(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop, // Ajusta según tu diseño
        modifier = Modifier.fillMaxSize()
    )
    val lecciones = listOf(
        Leccion("Fundamentos", "Curso 1", completada = true),
        Leccion("Abecedario", "Curso 1", completada = false),
        Leccion("Saludos", "Curso 1", completada = false)
    )

    ListaLecciones(lecciones)
}

data class Leccion(
    val titulo: String,
    val curso: String,
    val completada: Boolean
)

@Composable
fun ListaLecciones(lecciones: List<Leccion>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(lecciones) { leccion ->
            TarjetaLeccion(leccion)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TarjetaLeccion(leccion: Leccion) {
    Card(
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        /*Image(
                            painter = painterResource(id = R.drawable.ic_hand), // IMAGEN DE LA MANO, CAMBIAR
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )*/
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Lección 1")
                            Text(
                                leccion.titulo,
                                fontWeight = FontWeight.Bold
                            )
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
    }
}
