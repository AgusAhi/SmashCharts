package com.example.smashchartss

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.smashchartss.ui.theme.FontTittle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSelectScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") } // Query de búsqueda

    // Filtrar personajes basados en la búsqueda
    val filteredList = characterFetch().filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Título en la parte superior
        Text(
            text = "CHOOSE YOUR CHARACTER",
            style = TextStyle(
                fontFamily = FontTittle,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        // Campo de búsqueda
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            placeholder = { Text("Search Characters...") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray.copy(alpha = 0.2f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Grid de personajes filtrados
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredList) { character ->
                CharacterCard(character) {
                    navController.navigate("menuScreen/${character.id}")
                }
            }
        }
    }
}

@Composable
fun characterFetch(): List<Character> {
    var characterList by remember { mutableStateOf<List<Character>>(emptyList()) } // Lista de personajes

    // Obtener la lista de personajes
    LaunchedEffect(key1 = true) {
        characterList = fetchCharacters()
    }
    return characterList
}

@Composable
fun CharacterCard(character: Character, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(70.dp) // Tamaño compacto para cada icono
            .clip(CircleShape) // Forma circular para los íconos
            .background(Color.LightGray) // Color de fondo para placeholders
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = character.icon_url,
            contentDescription = "Character Icon",
            modifier = Modifier.size(64.dp), // Tamaño ajustado para la imagen
            contentScale = ContentScale.Crop
        )
    }
}




