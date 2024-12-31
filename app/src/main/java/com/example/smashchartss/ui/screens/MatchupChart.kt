@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smashchartss.ui.screens

import android.content.res.Configuration
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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

// Pantalla de Matchup Chart
@RequiresApi(35)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchupChart(characterId: String?, navHostController: NavHostController) {
    val isEditMode = remember { mutableStateOf(false) }
    val allCharacters = remember { mutableStateOf<List<Character>>(emptyList()) }
    val availableCharacters = remember { mutableStateListOf<Character>() }
    val boxes = remember { mutableStateListOf(BoxState("Default Box", mutableStateListOf())) }
    val showDialog = remember { mutableStateOf(false) } // Estado para el diálogo
    var selectedCharacter by remember { mutableStateOf<Character?>(null) } // Personaje seleccionado
    val currentCharacter = allCharacters.value.find { it.id == characterId }


    // Cargar personajes
    CharacterFetchRemovable(allCharacters, availableCharacters, characterId)


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
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentCharacter?.name ?: "Matchup Chart", // Título dinámico
                            style = TextStyle(
                                fontFamily = FontTittle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            val rotation by animateFloatAsState(
                targetValue = if (isEditMode.value) 360f else 0f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ),
                label = "FAB Rotation"
            )


            val scale by animateFloatAsState(
                targetValue = if (isEditMode.value) 1f else 1.5f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "FAB Scale"
            )


            FloatingActionButton(
                onClick = { isEditMode.value = !isEditMode.value },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Box(
                    modifier = Modifier
                        .rotate(rotation)
                        .scale(scale), // Aplica tanto rotación como escala
                    contentAlignment = Alignment.Center
                ) {
                    if (isEditMode.value) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Mode",
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "View Mode",
                            tint = Color.White
                        )
                    }
                }
            }
        }


    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount > 50) {
                                navHostController.navigateUp()
                            }
                        }
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))


                // Mostrar carta del personaje seleccionado
                currentCharacter?.let {
                    CharacterCard(character = it, onClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                }


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


                // Mostrar cajas dinámicamente
                boxes.forEach { box ->
                    BoxContent(
                        boxState = box,
                        isEditMode = isEditMode.value,
                        onRemoveCharacter = { character ->
                            availableCharacters.addFirst(character)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }


                // Botones para añadir y quitar cajas en la misma fila
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botón para añadir nueva caja
                    FloatingActionButton(
                        onClick = { boxes.add(BoxState("New Box", mutableStateListOf())) },
                        modifier = Modifier
                            .padding(8.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text("+", color = Color.White, fontSize = 24.sp)
                    }


                    // Botón para eliminar la última caja
                    FloatingActionButton(
                        onClick = {
                            if (boxes.isNotEmpty()) {
                                val lastBox = boxes.last() // Obtén la última caja
                                availableCharacters.addAll(0, lastBox.characters) // Devuelve los personajes al inicio de la lista
                                boxes.removeLast() // Elimina la caja
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text("-", color = Color.White, fontSize = 24.sp)
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))


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
                        if (isEditMode.value) {
                            selectedCharacter = character
                            showDialog.value = true // Mostrar el diálogo
                        }
                    }
                )
            }
        }


        // Mostrar el diálogo si está activado
        if (showDialog.value) {
            showBoxSelectionDialog(
                boxes = boxes,
                character = selectedCharacter!!,
                onAssignCharacter = { selectedBox ->
                    selectedBox.characters.add(selectedCharacter!!)
                    availableCharacters.remove(selectedCharacter!!)
                },
                onDismiss = { showDialog.value = false }
            )
        }
    }
}




// Clase para representar una caja con título y lista de personajes asignados
data class BoxState(var title: String, val characters: SnapshotStateList<Character>)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BoxContent(
    boxState: BoxState,
    isEditMode: Boolean,
    onRemoveCharacter: (Character) -> Unit
) {
    // Estado local para manejar el título editable de la caja
    var editableTitle by remember { mutableStateOf(boxState.title) }
    val focusManager = LocalFocusManager.current // Gestionar el foco


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color.LightGray, // Fondo gris
                shape = RoundedCornerShape(12.dp) // Bordes redondeados
            )
            .border(
                width = 1.dp,
                color = Color.Gray, // Borde gris más oscuro
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .clickable { focusManager.clearFocus() }, // Oculta el teclado al hacer clic fuera del TextField
    ) {
        TextField(
            value = editableTitle,
            onValueChange = { newTitle ->
                editableTitle = newTitle // Actualiza el título localmente
                boxState.title = newTitle // Sincroniza con el estado del BoxState
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface, // Color del texto dinámico
                textAlign = TextAlign.Center // Centrar texto
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable { /* Evitar que otros clics cierren el teclado */ },
            placeholder = {
                Text(
                    "Enter box title",
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Gray
            )
        )


        // Contenedor de los personajes
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            boxState.characters.forEachIndexed { index, character ->
                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CharacterCard(character = character, onClick = {
                        if (isEditMode) {
                            boxState.characters.remove(character)
                            onRemoveCharacter(character)
                        }
                    })
                }


                if ((index + 1) % 5 == 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}






@Composable
fun showBoxSelectionDialog(
    boxes: List<BoxState>,
    character: Character,
    onAssignCharacter: (BoxState) -> Unit,
    onDismiss: () -> Unit // Nueva función para manejar el cierre del diálogo
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Select a Box",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                boxes.forEach { box ->
                    Text(
                        text = box.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onAssignCharacter(box)
                                onDismiss() // Cierra el diálogo tras seleccionar
                            },
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    )
                }
            }
        },
        confirmButton = {
            Text(
                text = "Cancel",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    )
}


@Composable
fun CharactersBoxClickable(
    filteredList: List<Character>,
    onCharacterClick: (Character) -> Unit,
    columns: Int = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) 8 else 6, // Más columnas en landscape
    isEditMode: Boolean = false,
    navHostController: NavHostController? = null
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val rows = filteredList.chunked(columns)


        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val adjustedRow = row + List(columns - row.size) { null }


                    adjustedRow.forEach { character ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(if (isLandscape) 0.8f else 1f) // Rectangular en landscape
                        ) {
                            if (character != null) {
                                CharacterCardClickable(
                                    character = character,
                                    isEditMode = isEditMode,
                                    navHostController = navHostController
                                ) {
                                    onCharacterClick(character)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}










@Composable
fun CharacterFetchRemovable(
    allCharacters: MutableState<List<Character>>,
    availableCharacters: SnapshotStateList<Character>,
    excludedCharacterId: String? // Nuevo parámetro para excluir el personaje
) {
    LaunchedEffect(key1 = Unit) {
        val characters = fetchCharacters() // Función que obtiene los personajes
        allCharacters.value = characters
        availableCharacters.addAll(
            characters.filter { it.id != excludedCharacterId } // Filtra el personaje excluido
        )


    }
}


@Composable
fun CharacterCardClickable(
    character: Character,
    isEditMode: Boolean,
    navHostController: NavHostController?,
    onClick: (() -> Unit)? = null
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    Box(
        modifier = Modifier
            .size(if (isLandscape) 90.dp else 70.dp) // Íconos más grandes en landscape
            .clip(RoundedCornerShape(64.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .clickable {
                if (!isEditMode && navHostController != null) {
                    navHostController.navigate("characterDetails/${character.id}")
                } else {
                    onClick?.invoke()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.icon_url)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = "Character Icon",
            modifier = Modifier.size(if (isLandscape) 80.dp else 64.dp),
            contentScale = ContentScale.Crop
        )


        if (isEditMode) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
            Text(
                text = "Edit",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}















