package com.example.anfeca.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.anfeca.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RecuperacionContrasena(navController: NavController) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recuperar contraseña", style = MaterialTheme.typography.headlineSmall, color = Color.White)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Correo electrónico", color = Color.White) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF8000),  // Naranja
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color(0xFFFF8000),
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()


        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val correo = email.text.trim()
                if (correo.isNotBlank()) {
                    FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(correo)
                        .addOnCompleteListener { task ->
                            mensaje = if (task.isSuccessful) {
                                "Si el correo está registrado, recibirás un enlace de recuperación."
                            } else {
                                "Error al enviar el correo. Verifica el formato o intenta más tarde."
                            }
                        }
                } else {
                    mensaje = "Ingresa un correo válido."
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
            Text("Enviar correo de recuperación")
        }


        Spacer(modifier = Modifier.height(16.dp))

        mensaje?.let {
            Text(text = it, color = if (it.contains("enviado")) Color.Green else Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Volver al inicio de sesión", color = Color.White)
        }
    }
}
