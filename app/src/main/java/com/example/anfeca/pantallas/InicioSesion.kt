package com.example.anfeca.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.anfeca.ui.theme.AnfecaTheme
import androidx.compose.material3.OutlinedTextFieldDefaults


@Composable
fun InicioSesion(navController: NavController) {
    AnfecaTheme{
    var paso by remember { mutableStateOf(0) }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val titulo = when (paso) {
        0 -> "¿Cómo te llamas?"
        1 -> "Inicia sesión"
        2 -> "Inicia sesión\n$email"
        else -> ""
    }

    val placeholder = when (paso) {
        0 -> "Nombre"
        1 -> "Dirección de email"
        2 -> "Contraseña"
        else -> ""
    }

    val icono = when (paso) {
        0 -> Icons.Default.Person
        1 -> Icons.Default.Email
        2 -> Icons.Default.Lock
        else -> Icons.Default.Person
    }

    val textoBoton = if (paso < 2) "Continuar" else "Iniciar sesión"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = when (paso) {
                0 -> nombre
                1 -> email
                2 -> password
                else -> ""
            },
            onValueChange = {
                when (paso) {
                    0 -> nombre = it
                    1 -> email = it
                    2 -> password = it
                }
            },
            leadingIcon = {
                Icon(imageVector = icono, contentDescription = null, tint = Color.White)
            },
            placeholder = { Text(placeholder, color = Color.White) },
            visualTransformation = if (paso == 2) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                Color.White,
                Color.White,
                Color(0xFFFF8000),
                Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (paso == 2) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (paso < 2) {
                    paso++
                } else {
                    // Ir al menú principal
                    navController.navigate("menu_principal")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF8000),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(textoBoton)
        }

        if (paso == 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ya tienes una cuenta? ",
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "Iniciar sesión",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    // Acción opcional
                }
            )
        }
    }
}
}
