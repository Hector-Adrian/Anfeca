package com.example.anfeca.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CursosPantalla() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text("Elegir un curso", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        val cursos = listOf(
            "Principiante I" to "1 lección completada",
            "Principiante II" to "7 unidades",
            "Elemental I" to "8 unidades",
            "Elemental II" to "7 unidades",
            "Intermedio" to "15 unidades",
            "Intermedio alto" to "15 unidades"
        )

        cursos.forEachIndexed { index, (nombre, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = listOf("A1", "A1", "A2", "A2", "B1", "B2")[index],
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Gray, shape = CircleShape)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(nombre, color = Color.White)
                        Text(desc, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
