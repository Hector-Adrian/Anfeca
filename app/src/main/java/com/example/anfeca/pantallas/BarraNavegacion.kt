package com.example.anfeca.pantallas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

data class BotonesBarraNavegacion(val route: String, val icon: ImageVector, val label: String)

val barraNavegacions = listOf(
    BotonesBarraNavegacion("Repaso", Icons.Default.DateRange, "Repaso"),
    BotonesBarraNavegacion("PantallaInicio", Icons.Default.Home, "Inicio"),
    BotonesBarraNavegacion("Asistente", Icons.Default.Person, "Asistente"),
    BotonesBarraNavegacion("Explorar", Icons.Default.Search, "Explorar")
)

@Composable
fun BarraNavegacion(navController: NavController, currentRoute: String) {
    NavigationBar(
        containerColor = Color(0xFFF9F9F9)
    ) {
        barraNavegacions.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("PantallaInicio") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label)
                },
                label = { Text(item.label) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFF8000),
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
