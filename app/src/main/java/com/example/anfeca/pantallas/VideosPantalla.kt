package com.example.anfeca.pantallas

import androidx.compose.foundation.background
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.core.net.toUri
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.anfeca.R
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



@Composable
fun VideosPantalla() {
    val db = FirebaseFirestore.getInstance()
    var videos by remember { mutableStateOf(emptyList<Curso>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        db.collection("Cursos")
            .get()
            .addOnSuccessListener { result ->
                videos = result.mapNotNull { it.toObject(Curso::class.java) }
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
        Text("Explorar Videos", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        videos.forEach { video ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, video.urlVideo.toUri())
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
                        text = video.nivel,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Gray, shape = CircleShape)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(video.titulo, color = Color.White)
                        Text(video.descripcion, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

