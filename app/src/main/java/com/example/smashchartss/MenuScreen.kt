package com.example.smashchartss

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smashchartss.ui.theme.FontTittle

@Composable
fun MenuScreen(characterId: String?, navHostController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "MOde Menu",
            style = TextStyle(
                fontFamily = FontTittle,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para navegar al "Matchup Chart"
        Button(
            onClick = {
                navHostController.navigate("matchupChart/$characterId")
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ) {
            Text("Matchup Chart")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Mostrar la carta del personaje seleccionado
        characterFetch().find { it.id == characterId }?.let {
            CharacterCard(character = it) {
                // Acción si quieres que algo pase al hacer clic en la card aquí
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


    }
}

