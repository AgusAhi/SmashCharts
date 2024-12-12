package com.example.smashchartss

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(characterId: String, navHostController: NavHostController) {
    val currentCharacter = remember { mutableStateOf<Character?>(null) }
    val allCharacters = remember { mutableStateOf<List<Character>>(emptyList()) }
    val availableCharacters = remember { mutableStateListOf<Character>() }

    // Lógica para obtener los datos del personaje
    CharacterFetchForDetails(
        characterId = characterId,
        currentCharacter = currentCharacter,
        allCharacters = allCharacters,
        availableCharacters = availableCharacters
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go Back", tint = Color.White)
                    }
                },
                title = {
                    Text(
                        text = "Character Details",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Details for character: $characterId",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}



@Composable
fun CharacterFetchForDetails(
    characterId: String,
    currentCharacter: MutableState<Character?>,
    allCharacters: MutableState<List<Character>>,
    availableCharacters: SnapshotStateList<Character>
) {
    LaunchedEffect(key1 = characterId) {
        try {
            Log.d("CharacterFetchForDetails", "Fetching characters for ID: $characterId")
            val characters = fetchCharacters() // Tu lógica para obtener los personajes
            Log.d("CharacterFetchForDetails", "Characters fetched: ${characters.size}")

            allCharacters.value = characters

            val selectedCharacter = characters.find { it.id == characterId }
            currentCharacter.value = selectedCharacter
            availableCharacters.clear()
            availableCharacters.addAll(characters.filter { it.id != characterId })

            if (selectedCharacter == null) {
                Log.e("CharacterFetchForDetails", "Character not found for ID: $characterId")
            }
        } catch (e: Exception) {
            Log.e("CharacterFetchForDetails", "Error fetching characters: ${e.message}")
        }
    }
}




