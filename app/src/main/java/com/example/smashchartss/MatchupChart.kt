@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smashchartss

import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
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
import com.example.smashchartss.ui.theme.FontTittle

@RequiresApi(35)
@Composable
fun MatchupChart(characterId: String?, navHostController: NavHostController) {
    val isEditMode = remember { mutableStateOf(false) } // Estado para alternar el modo edición
    val assignedCharacters = remember { mutableStateListOf<Character>() } // Lista de personajes asignados
    val allCharacters = remember { mutableStateOf<List<Character>>(emptyList()) }
    val availableCharacters = remember { mutableStateListOf<Character>() }

    // Cargar los personajes al iniciar la composición
    CharacterFetchRemovable(allCharacters, availableCharacters)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Título general de la pantalla
            Text(
                text = "Matchup Chart",
                style = TextStyle(
                    fontFamily = FontTittle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Caja principal para los personajes asignados
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
                    .border(2.dp, Color.Black)
                    .padding(8.dp)
            ) {

                // Lógica específica de la caja principal
                BoxContent(
                    isEditMode = isEditMode.value,
                    assignedCharacters = assignedCharacters,
                    availableCharacters = availableCharacters
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Otros elementos de la UI (personajes disponibles, botones, etc.)
            Text(
                text = "Available Characters",
                style = TextStyle(
                    fontFamily = FontTittle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            CharactersBoxClickable(
                filteredList = availableCharacters,
                onCharacterClick = { character ->
                    if (character !in assignedCharacters && isEditMode.value) {
                        assignedCharacters.add(character) // Agrega directamente
                        availableCharacters.remove(character) // Remueve directamente
                    }
                }
            )

        }

        // Floating Action Button (FAB) fuera del contenido principal
        FloatingActionButton(
            onClick = { isEditMode.value = !isEditMode.value },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Posicionar en la esquina inferior derecha
                .padding(16.dp),
            containerColor = if (isEditMode.value) Color.Green else Color.Gray
        ) {
            Text(
                text = if (isEditMode.value) "Done" else "Edit",
                color = Color.White
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(35)
@Composable
fun BoxContent(
    isEditMode: Boolean,
    assignedCharacters: SnapshotStateList<Character>,
    availableCharacters: SnapshotStateList<Character>
) {
    // Estado para almacenar el nombre editable de la caja
    var boxTitle by remember { mutableStateOf("Assigned Characters") }

    Column(modifier = Modifier.fillMaxSize()) {
        // TextField para editar el nombre de la caja
        TextField(
            value = boxTitle,
            onValueChange = { newValue -> boxTitle = newValue },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Enter box name",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        // Mostrar personajes asignados dentro de la caja
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (assignedCharacters.isEmpty()) {
                // Mensaje si no hay personajes asignados
                Text(
                    text = "Click on a character to add them here",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Organizar personajes en un flujo dinámico
                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    assignedCharacters.forEach { character ->
                        Box(
                            modifier = Modifier
                                .size(60.dp) // Tamaño fijo para cada personaje
                                .aspectRatio(1f) // Mantén la relación de aspecto cuadrada
                        ) {
                            CharacterCard(character = character, onClick = {
                                if (isEditMode) {
                                    assignedCharacters.remove(character) // Actualiza el estado
                                    availableCharacters.addFirst(character)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun CharactersBoxClickable(
    filteredList: List<Character>,
    onCharacterClick: (Character) -> Unit,
    columns: Int = 6 // Define cuántas columnas tendrá la "cuadrícula"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Agrupa los personajes en filas
        val rows = filteredList.chunked(columns)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { character ->
                        Box(
                            modifier = Modifier
                                .weight(1f) // Divide el espacio disponible entre las columnas
                                .aspectRatio(1f) // Mantiene una relación de aspecto cuadrada
                        ) {
                            CharacterCard(character) {
                                onCharacterClick(character)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterFetchRemovable(
    allCharacters: MutableState<List<Character>>,
    availableCharacters: SnapshotStateList<Character>
) {
    LaunchedEffect(key1 = Unit) {
        val characters = fetchCharacters() // Función que obtiene los personajes
        allCharacters.value = characters
        availableCharacters.addAll(characters)
    }
}
