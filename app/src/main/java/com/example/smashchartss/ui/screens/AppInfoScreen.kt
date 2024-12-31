package com.example.smashchartss.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import com.example.smashchartss.R
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun AppInfoScreen(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Hacer que el contenido sea desplazable
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 50) {
                        navHostController.navigateUp()
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título
        Text(
            text = "App Info",
            style = MaterialTheme.typography.headlineMedium, // Fuente predeterminada para títulos
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de ejemplo (sustituible)
        Image(
            painter = painterResource(id = R.drawable.osagechan),
            contentDescription = "Logo o imagen informativa",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Texto de Créditos
        Text(
            text = "By: BravelyU",
            style = MaterialTheme.typography.bodyLarge, // Fuente predeterminada para cuerpo de texto
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Información adicional
        Text(
            text = """
                Versión: 1.0.0
                
                This application is built using the Jetpack Compose framework with Kotlin as the primary programming language.
                
                It leverages Supabase for database management, integrated via the Ktor client.
                
                The app also incorporates Google Accompanist for seamless UI enhancements,
                Material3 for modern design components, and Kotlin Coroutines for efficient asynchronous programming.
                
                Image loading is handled using the Coil library, and Navigation Compose facilitates smooth and dynamic screen transitions.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium, // Otro estilo predeterminado para texto informativo
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para volver
        Button(
            onClick = { navHostController.navigateUp() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Back to Menu")
        }
    }
}
