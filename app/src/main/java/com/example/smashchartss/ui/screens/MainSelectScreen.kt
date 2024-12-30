package com.example.smashchartss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.smashchartss.data.models.Character
import com.example.smashchartss.data.remote.fetchCharacters
import com.example.smashchartss.ui.theme.FontTittle

@Composable
fun MainSelectScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") } // Query de búsqueda


    // Filtrar personajes basados en la búsqueda
    val filteredList = characterFetch().filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }


    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // Título en la parte superior
            Text(
                text = "CHOOSE YOUR CHARACTER",
                style = TextStyle(
                    fontFamily = FontTittle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 45.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )


            // Campo de búsqueda con ícono de lupa
            SearchField(searchQuery) { query -> searchQuery = query }


            // Grid de personajes filtrados adaptado a la orientación
            if (isLandscape) {
                CharactersGrid(filteredList, navController, columns = 8)
            } else {
                CharactersGrid(filteredList, navController, columns = 4)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(searchQuery: String, onSearchQueryChanged: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text(
                text = "Search Characters...",
                style = TextStyle(color = Color.Gray)
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.LightGray.copy(alpha = 0.2f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Black
            )
        }
    )
}


@Composable
fun CharactersGrid(filteredList: List<Character>, navController: NavHostController, columns: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
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
            .clip(RoundedCornerShape(64.dp))
            .background(Color.LightGray) // Color de fondo para placeholders
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.icon_url)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED) // Habilita caché en disco
                .memoryCachePolicy(CachePolicy.ENABLED) // Habilita caché en memoria
                .build(),
            contentDescription = "Character Icon",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )


    }
}
