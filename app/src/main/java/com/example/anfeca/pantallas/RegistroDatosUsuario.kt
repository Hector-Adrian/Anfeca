package com.example.anfeca.pantallas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.anfeca.R
import com.example.anfeca.datos.crearProgresoUsuario
import com.example.anfeca.datos.crearUsuario
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


@Composable
fun RegistroDatosUsuario(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop, // Ajusta según tu diseño
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de usuario", fontSize = 22.sp, color = Color.White)

        Spacer(modifier = Modifier.height(24.dp))

        CampoTexto("Nombre", nombre) { nombre = it }
        CampoTexto("Correo electrónico", correo) { correo = it }
        CampoTexto("Contraseña", contrasena, isPassword = true) { contrasena = it }

        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current

        Button(
            onClick = {
                if (correo.isNotBlank() && contrasena.length >= 6 && nombre.isNotBlank()) {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(correo, contrasena)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val userId = user?.uid

                                val db = Firebase.firestore
                                val datosUsuario = hashMapOf(
                                    "nombre" to nombre,
                                    "correo" to correo
                                )


                                db.collection("Usuarios").document(userId!!)
                                    .set(datosUsuario)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("PantallaInicio")
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Error al guardar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        crearUsuario(nombre,correo)
                        crearProgresoUsuario(nombre)
                } else {
                    Toast.makeText(context, "Completa todos los campos y usa contraseña de al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8000))
        )
        {
            Text("Registrarse")
        }
    }
}

@Composable
fun CampoTexto(
    label: String,
    valor: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFF8000),
            unfocusedBorderColor = Color.White,
            focusedLabelColor = Color(0xFFFF8000),
            unfocusedLabelColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )

}
