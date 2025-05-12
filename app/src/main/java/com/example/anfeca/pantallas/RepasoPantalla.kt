package com.example.anfeca.pantallas

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.anfeca.R

@Composable
fun RepasoPantalla(navController: NavController) {
    val context = LocalContext.current
    var permisoConcedido by remember { mutableStateOf(false) }

    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoConcedido = concedido
    }

    LaunchedEffect(Unit) {
        val permisoActual = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permisoActual != PackageManager.PERMISSION_GRANTED) {
            permisoLauncher.launch(Manifest.permission.CAMERA)
        } else {
            permisoConcedido = true
        }
    }

    if (permisoConcedido) {
        MostrarVistaCamara()
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Se necesita permiso de cámara para usar esta función.", color = Color.White)
        }
    }
}

@Composable
fun MostrarVistaCamara() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    // Lista de imágenes (asegúrate de tener estas imágenes en drawable)
    val imagenesSenias = listOf(
        R.drawable.sena_a,
        R.drawable.sena_b,
        R.drawable.sena_c
    )
    val imagenAleatoria = remember { imagenesSenias.random() }

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        } catch (e: Exception) {
            Log.e("Camera", "Error al iniciar cámara", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        Text(
            "Realiza la siguiente seña",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imagenAleatoria),
                contentDescription = "Seña aleatoria",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

