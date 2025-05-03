package com.example.anfeca.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RecursosPantalla(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text("Explora", color = Color.White, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("cursos") },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA861))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cursos", color = Color.White)
                Text(
                    "Aquí encontrarás todos los cursos según tu nivel, habilidades e intereses.",
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Mira y aprende", color = Color.White)

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("videos") },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD9C2FF))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Videos", color = Color.Black)
                Text("Aprovecha al máximo tus ratos libres.", color = Color.Black)
            }
        }
    }
}
