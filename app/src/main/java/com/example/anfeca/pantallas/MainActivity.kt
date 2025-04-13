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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import com.google.firebase.FirebaseApp

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
                AppNavigation()
        }
    }
}


@Composable
fun CameraPreview() {
    // Usamos AndroidView para insertar PreviewView en Compose
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

    NavHost(navController = navController, startDestination = "splash") {
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
            LeccionPantalla(leccionId = leccionId, navController = navController)
        }
        composable("RecuperacionContrasena") { RecuperacionContrasena(navController)  }

    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000) // Espera 3 segundos
        navController.navigate("registro") {
            popUpTo("splash") { inclusive = true } // Elimina esta pantalla del backstack
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Cargando...", color = Color.White)
    }
}