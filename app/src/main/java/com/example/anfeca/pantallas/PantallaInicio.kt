package com.example.anfeca.pantallas


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.anfeca.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

data class Leccion(
    val id: String = "",
    val titulo: String = "",
    val curso: String = "",
    val completada: Boolean = false
)

data class ProgresoUsuario(
    val curso1: MutableMap<String, Boolean> = mutableMapOf(),
    val curso2: MutableMap<String, Boolean> = mutableMapOf(),
    val curso3: MutableMap<String, Boolean> = mutableMapOf()
)

@Composable
fun PantallaInicio(navController: NavController) {
    val scope = rememberCoroutineScope()
    var lecciones by remember { mutableStateOf<List<Leccion>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val coloresCursos = mapOf(
        "Curso 1: Básico" to Color(0xFFFDE7B0),
        "Curso 2: Intermedio" to Color(0xFFB0E3FD),
        "Curso 3: Avanzado" to Color(0xFFD7B0FD)
    )

    // Cargar lecciones
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val progreso = obtenerProgresoUsuario()

                val listaCursos = listOf("LeccionesCurso1", "LeccionesCurso2", "LeccionesCurso3")
                val todasLasLecciones = mutableListOf<Leccion>()

                for (nombreColeccion in listaCursos) {
                    val cursoNum = nombreColeccion.substringAfter("LeccionesCurso").toInt()
                    val completadasCurso = when (cursoNum) {
                        1 -> progreso.curso1
                        2 -> progreso.curso2
                        3 -> progreso.curso3
                        else -> emptyMap()
                    }

                    val leccionesCurso = db.collection(nombreColeccion)
                        .get()
                        .await()
                        .documents
                        .map { doc ->
                            Leccion(
                                id = doc.id,
                                titulo = doc.getString("titulo") ?: "Lección sin título",
                                curso = doc.getString("curso") ?: "Curso $cursoNum: Sin nombre",
                                completada = completadasCurso[doc.id] == true
                            )
                        }

                    todasLasLecciones.addAll(leccionesCurso)
                }

                val leccionesOrdenadas = todasLasLecciones.sortedWith(
                    compareBy(
                        { it.curso.substringBefore(":").substringAfter("Curso ").toIntOrNull() ?: 0 },
                        { it.id }
                    )
                )

                lecciones = leccionesOrdenadas
                cargando = false
            } catch (e: Exception) {
                error = "Error al cargar lecciones: ${e.message}"
                cargando = false
            }
        }
    }

    // Fondo
    Image(
        painter = painterResource(id = R.drawable.registro_se),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¡Bienvenido!",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            IconButton(onClick = {
                navController.navigate("PerfilUsuario")
            }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil de usuario",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Contenido principal
        when {
            cargando -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "¡Ups! Algo salió mal",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = error ?: "Error desconocido")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                cargando = true
                                error = null
                                scope.launch {
                                }
                            }) {
                                Text("Intentar de nuevo")
                            }
                        }
                    }
                }
            }
            else -> {
                ListaLecciones(
                    lecciones = lecciones,
                    navController = navController,
                    coloresCursos = coloresCursos
                )
            }
        }
    }
}

@Composable
fun ListaLecciones(
    lecciones: List<Leccion>,
    navController: NavController,
    coloresCursos: Map<String, Color>
) {
    val leccionesPorCurso = lecciones.groupBy { it.curso }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        leccionesPorCurso.forEach { (curso, leccionesCurso) ->
            item {
                TarjetaCurso(
                    curso = curso,
                    lecciones = leccionesCurso,
                    navController = navController,
                    colorCurso = coloresCursos[curso] ?: Color(0xFFFDE7B0)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

suspend fun obtenerProgresoUsuario(): ProgresoUsuario {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userEmail = auth.currentUser?.email ?: return ProgresoUsuario()

    return try {
        // Documento del usuario
        val userQuery = db.collection("Usuarios")
            .whereEqualTo("email", userEmail)
            .get()
            .await()

        val userDoc = userQuery.documents.firstOrNull()
        val nombreUsuario = userDoc?.id

        if (nombreUsuario != null) {
            val progreso = ProgresoUsuario()

            //Curso 1
            try {
                val curso1Doc = db.collection("Usuarios")
                    .document(nombreUsuario)
                    .collection("Progreso")
                    .document("Curso1")
                    .get()
                    .await()

                if (curso1Doc.exists() && curso1Doc.data != null) {
                    curso1Doc.data?.forEach { (key, value) ->
                        if (value is Boolean) {
                            progreso.curso1[key] = value
                        }
                    }
                }
            } catch (e: Exception) {
            }

            // Curso 2
            try {
                val curso2Doc = db.collection("Usuarios")
                    .document(nombreUsuario)
                    .collection("Progreso")
                    .document("Curso2")
                    .get()
                    .await()

                if (curso2Doc.exists() && curso2Doc.data != null) {
                    curso2Doc.data?.forEach { (key, value) ->
                        if (value is Boolean) {
                            progreso.curso2[key] = value
                        }
                    }
                }
            } catch (e: Exception) {
            }

            // Curso 3
            try {
                val curso3Doc = db.collection("Usuarios")
                    .document(nombreUsuario)
                    .collection("Progreso")
                    .document("Curso3")
                    .get()
                    .await()

                if (curso3Doc.exists() && curso3Doc.data != null) {
                    curso3Doc.data?.forEach { (key, value) ->
                        if (value is Boolean) {
                            progreso.curso3[key] = value
                        }
                    }
                }
            } catch (e: Exception) {
            }

            progreso
        } else {
            ProgresoUsuario()
        }
    } catch (e: Exception) {

        ProgresoUsuario()
    }
}

@Composable
fun TarjetaCurso(
    curso: String,
    lecciones: List<Leccion>,
    navController: NavController,
    colorCurso: Color
) {
    val leccionesCompletadas = lecciones.count { it.completada }
    val porcentajeCompletado = if (lecciones.isNotEmpty()) {
        (leccionesCompletadas.toFloat() / lecciones.size) * 100
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = colorCurso)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = curso,
                    color = Color(0xFF1B3A4B),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${porcentajeCompletado.toInt()}% completado",
                    color = Color(0xFF1B3A4B),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            LinearProgressIndicator(
                progress = { porcentajeCompletado / 100 },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(6.dp),
                color = Color(0xFFFF8000),
                trackColor = Color(0x33000000)
            )

            Spacer(modifier = Modifier.height(8.dp))

            lecciones.forEach { leccion ->
                Card(
                    onClick = {
                        // Guardar el ID del curso
                        navController.currentBackStackEntry?.savedStateHandle?.set("cursoId", curso)
                        // Navegar a la pantalla de lección
                        navController.navigate("Leccion/${leccion.id}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = leccion.titulo,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
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
}