package com.example.anfeca.pantallas

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.net.toUri
import com.example.anfeca.R

data class Curso(
    val titulo: String = "",
    val descripcion: String = "",
    val nivel: String = "",
    val numeroDeLecciones: Int = 0,
    val urlPdf: String = "",
    val urlVideo: String = ""
)

@Composable
fun CursosPantalla() {
    val db = FirebaseFirestore.getInstance()
    var cursos by remember { mutableStateOf<List<Curso>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        db.collection("Cursos")
            .get()
            .addOnSuccessListener { result ->
                cursos = result.mapNotNull { it.toObject(Curso::class.java) }
            }
    }
    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text("Elegir un curso", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        cursos.forEach { curso ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, curso.urlPdf.toUri())
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = curso.nivel,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Gray, shape = CircleShape)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(curso.titulo, color = Color.White)
                        Text(curso.descripcion, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

