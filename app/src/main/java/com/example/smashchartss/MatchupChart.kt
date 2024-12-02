package com.example.smashchartss

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MatchupChart(characters: List<Character>) {
    // Estado para rastrear el personaje arrastrado y su posición
    var draggedCharacter by remember { mutableStateOf<Character?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var initialOffset by remember { mutableStateOf(Offset.Zero) }

    // Lista de personajes que se han soltado en la zona de drop
    val droppedCharacters = remember { mutableStateListOf<Character>() }

    // Índice del personaje actual mostrado en la fila inferior
    var currentCharacterIndex by remember { mutableStateOf(0) }

    // ID del personaje seleccionado
    var selectedCharacterId by remember { mutableStateOf<String?>(null) }

    // Contenedor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Zona de drop en la parte superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .align(Alignment.TopCenter)
                .background(Color(0xFFEFEFEF))
        ) {
            if (droppedCharacters.isEmpty()) {
                Text(
                    text = "Drop Characters Here",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    droppedCharacters.forEach { character ->
                        AsyncImage(
                            model = character.icon_url,
                            contentDescription = "Dropped Character",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // Sección de un solo personaje en la parte inferior
        if (currentCharacterIndex < characters.size) {
            val character = characters[currentCharacterIndex]

            // Usar el personaje seleccionado si existe
            val displayCharacter = selectedCharacterId?.let { id ->
                characters.find { it.id == id }
            } ?: character

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)  // Makes the box square
                    .align(Alignment.BottomCenter)
                    .background(Color.LightGray)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = displayCharacter.icon_url,
                    contentDescription = "Character Icon",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .onGloballyPositioned { layoutCoordinates ->
                            val position = layoutCoordinates.localToRoot(Offset.Zero)
                            initialOffset = Offset(position.x, position.y)
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { startOffset ->
                                    draggedCharacter = displayCharacter
                                    selectedCharacterId = displayCharacter.id
                                    dragOffset = initialOffset
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume() // Consumir el cambio para evitar eventos adicionales
                                    dragOffset += dragAmount
                                },
                                onDragEnd = {
                                    if (dragOffset.y < 150.dp.toPx()) { // Verifica si está en la zona de drop
                                        droppedCharacters.add(displayCharacter)
                                        currentCharacterIndex++ // Muestra el siguiente personaje
                                    }
                                    draggedCharacter = null // Liberar el personaje
                                    dragOffset = Offset.Zero // Reiniciar la posición
                                }
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Personaje siendo arrastrado
        draggedCharacter?.let {
            AsyncImage(
                model = it.icon_url,
                contentDescription = "Dragged Character",
                modifier = Modifier
                    .size(64.dp)
                    .offset {
                        IntOffset(
                            dragOffset.x.toInt(),
                            dragOffset.y.toInt()
                        )
                    }
                    .clip(CircleShape)
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// Extensión para convertir Dp a Px
@Composable
private fun Float.toPx(): Float = this * androidx.compose.ui.platform.LocalDensity.current.density
