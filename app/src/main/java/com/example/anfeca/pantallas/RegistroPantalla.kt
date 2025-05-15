package com.example.anfeca.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.anfeca.R
import com.example.anfeca.ui.theme.Naranja

@Composable
fun RegistroPantalla(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.registro_se),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.inclunet),
                contentDescription = "Imagen de inicio",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(2.0f),
            )

            // Imagen
            Image(
                painter = painterResource(id = R.drawable.imagen_inicio),
                contentDescription = "Imagen de inicio",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1.1f),
                contentScale = ContentScale.Fit
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Exprésate con seguridad",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Practica desde la primera lección.",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { navController.navigate("cuestionario_registro") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Naranja,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Registrarse")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Text("¿Ya tienes una cuenta? ", color = Color.White)
                    Text(
                        "Iniciar sesión",
                        color = Naranja,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("inicio_sesion")
                        }
                    )
                }
            }
        }
    }
}


