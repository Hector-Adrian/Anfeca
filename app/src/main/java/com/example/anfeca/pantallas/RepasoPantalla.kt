package com.example.anfeca.pantallas;

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.anfeca.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

data class LeccionDiaria(
    val id: String = "",
    val titulo: String = "",
    val curso: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepasoPantalla(navController: NavController) {
    val scope = rememberCoroutineScope()
    var leccionDiaria by remember { mutableStateOf<LeccionDiaria?>(null) }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Color naranja para el fondo de la tarjeta
    val naranjaRepaso = Color(0xFFFF7F24)
    val fondoOscuro = Color(0xFF121212)

    // Cargar la lección diaria cuando se necesite (al presionar el botón)
    fun cargarLeccionDiaria() {
        cargando = true
        scope.launch {
            try {
                leccionDiaria = obtenerLeccionDiaria()
                cargando = false
                // Extraer el nombre del curso para pasar como cursoId
                val cursoStr = leccionDiaria?.curso ?: ""
                navController.currentBackStackEntry?.savedStateHandle?.set("cursoId", cursoStr)
                navController.navigate("Leccion/${leccionDiaria?.id}")
            } catch (e: Exception) {
                error = "Error al cargar la lección: ${e.message}"
                cargando = false
            }
        }
    }

    // Estructura completa de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repaso", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = fondoOscuro
                )
            )
        },
        containerColor = fondoOscuro
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de repaso diario
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = naranjaRepaso),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Título de la tarjeta
                    Text(
                        text = "Repaso diario",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Descripción
                    Text(
                        text = "Practica el vocabulario sugerido de entre las lecciones disponibles y entrena tu memoria al máximo",
                        fontSize = 16.sp,
                        color = Color.White,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón para comenzar
                    Button(
                        onClick = {
                            cargarLeccionDiaria()
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Comenzar",
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Diálogo de carga
            if (cargando) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Cargando repaso") },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = naranjaRepaso)
                            Text("Preparando tu lección diaria...")
                        }
                    },
                    confirmButton = { }
                )
            }

            // Diálogo de error
            if (error != null) {
                AlertDialog(
                    onDismissRequest = { error = null },
                    title = { Text("Error") },
                    text = { Text(error ?: "Ocurrió un error inesperado") },
                    confirmButton = {
                        TextButton(onClick = { error = null }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}

suspend fun obtenerLeccionDiaria(): LeccionDiaria {
    val db = FirebaseFirestore.getInstance()

    // Usar la fecha actual para generar una semilla para la selección aleatoria
    val fechaActual = obtenerFechaActual()
    val semillaDiaria = fechaActual.hashCode()
    val random = Random(semillaDiaria.toLong())

    // Lista para almacenar todas las lecciones
    val todasLasLecciones = mutableListOf<LeccionDiaria>()

    // Cargar las lecciones de los tres cursos
    val listaCursos = listOf("LeccionesCurso1", "LeccionesCurso2", "LeccionesCurso3")

    for (nombreColeccion in listaCursos) {
        val cursoNum = nombreColeccion.substringAfter("LeccionesCurso").toInt()
        val nombreCurso = when (cursoNum) {
            1 -> "Curso 1: Básico"
            2 -> "Curso 2: Intermedio"
            3 -> "Curso 3: Avanzado"
            else -> "Curso $cursoNum"
        }

        try {
            val leccionesCurso = db.collection(nombreColeccion)
                .get()
                .await()
                .documents
                .map { doc ->
                    LeccionDiaria(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: "Lección sin título",
                        curso = doc.getString("curso") ?: nombreCurso
                    )
                }

            todasLasLecciones.addAll(leccionesCurso)
        } catch (e: Exception) {
            // Continuar con el siguiente curso si hay un error
            continue
        }
    }

    // Si no hay lecciones disponibles, lanzar una excepción
    if (todasLasLecciones.isEmpty()) {
        throw Exception("No hay lecciones disponibles en ningún curso")
    }

    // Seleccionar una lección aleatoria basada en la semilla del día
    val indiceAleatorio = random.nextInt(todasLasLecciones.size)
    return todasLasLecciones[indiceAleatorio]
}

fun obtenerFechaActual(): String {
    val formato = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return formato.format(Date())
}