package com.example.anfeca.pantallas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.firebase.FirebaseApp

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.anfeca.datos.Notificaciones

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        scheduleDailyNotification()
        setContent {
                AppNavigation()
        }
    }

    fun scheduleDailyNotification() {
        val dailyRequest = PeriodicWorkRequestBuilder<Notificaciones>(15, TimeUnit.DAYS)
            .setInitialDelay(15, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )
    }
}


@Composable
fun CameraPreview() {
    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}





@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = currentBackStackEntry?.destination?.route
    val rutasConBarra = listOf("PantallaInicio", "Repaso", "Asistente", "Explorar","PerfilUsuario","Asistente","Recursos","cursos","videos")

    val mostrarBarraInferior = rutasConBarra.any { rutaActual?.startsWith(it) == true }

    Scaffold(
        bottomBar = {
            if (mostrarBarraInferior && rutaActual != null) {
                BarraNavegacion(navController, rutaActual)
            }
        },
        containerColor = Color.Black
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("registro") { RegistroPantalla(navController) }
            composable("cuestionario_registro") { CuestionarioRegistro(navController) }
            composable("inicio_sesion") { InicioSesion(navController) }
            composable("PantallaInicio") { PantallaInicio(navController) }
            composable("RegistroDatosUsuario") { RegistroDatosUsuario(navController) }
            composable(
                route = "Leccion/{leccionId}",
                arguments = listOf(navArgument("leccionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val leccionId = backStackEntry.arguments?.getString("leccionId") ?: ""
                val cursoId = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("cursoId") ?: ""

                LeccionPantalla(leccionId = leccionId, cursoId = cursoId, navController = navController)
            }
            composable("RecuperacionContrasena") { RecuperacionContrasena(navController) }
            composable("PerfilUsuario") { PerfilUsuario(navController) }
            composable("Repaso") { RepasoPantalla(navController) }
            composable("Camara") { CamaraPantalla(navController) }
            composable("Asistente") { AsistentePantalla(navController) }
            composable("Explorar") { ExplorarPantalla(navController) }
            composable("Recursos") { RecursosPantalla(navController) }
            composable("cursos") { CursosPantalla() }
            composable("videos") { VideosPantalla() }
        }
    }
}


@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)

        val usuario = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (usuario != null) {
            navController.navigate("PantallaInicio") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("registro") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Cargando...", color = Color.White)
    }
}


