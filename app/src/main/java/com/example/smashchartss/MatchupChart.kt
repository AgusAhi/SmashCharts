package com.example.smashchartss

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smashchartss.ui.theme.FontTittle


@Composable
fun MatchupChart(characterId: String?, navHostController: NavHostController) {
    val draggedCharacter = remember { mutableStateOf<Character?>(null) }
    val dragOffset = remember { mutableStateOf(Offset.Zero) }
    val isDragging = remember { mutableStateOf(false) }
    val boxes = remember { mutableStateListOf(BoxData()) } // Lista mutable para las cajas
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            // .verticalScroll(scrollState), PETTAAAAAAAAAAAAAAAAAA
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar la carta del personaje seleccionado
        characterFetch().find { it.id == characterId }?.let {
            CharacterCard(character = it) {
                // Acción al hacer clic en la carta
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Iterar sobre las cajas dinámicas
        boxes.forEach { boxData ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
                    .border(2.dp, Color.Black)
                    .padding(bottom = 8.dp)
            ) {
                if (draggedCharacter.value != null && isDragging.value && boxData.isActive) {
                    // Mostrar el personaje arrastrado de forma visual
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(dragOffset.value.x.toInt(), dragOffset.value.y.toInt()) }
                            .size(100.dp)
                            .background(Color.Gray, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = draggedCharacter.value?.name ?: "",
                            style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                } else {
                    // Mostrar el mensaje por defecto
                    Text(
                        text = "Drag characters here",
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // Botón para agregar una nueva caja
        Button(
            onClick = { boxes.add(BoxData()) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "+ Add Box")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = "Drag and Drop",
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

        CharactersGridDragable(
            filteredList = characterFetch(),
            navController = navHostController,
            onDragStart = { character ->
                draggedCharacter.value = character
                isDragging.value = true
            },
            onDrag = { change, dragAmount ->
                change.consume() // Indica que el evento fue manejado
                dragOffset.value += dragAmount
            },
            onDragEnd = {
                isDragging.value = false
                draggedCharacter.value = null
            },
            navigateTo = { character -> "charactersInfo/${character.id}" }
        )
    }
}

// Clase de datos para manejar las propiedades de cada caja
data class BoxData(val isActive: Boolean = true)


@Composable
fun CharactersGridDragable(
    filteredList: List<Character>,
    navController: NavHostController,
    onDragStart: (Character) -> Unit,
    onDrag: (PointerInputChange, Offset) -> Unit,
    onDragEnd: () -> Unit,
    navigateTo: (Character) -> String
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredList) { character ->
            Box(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { onDragStart(character) },
                            onDrag = onDrag,
                            onDragEnd = onDragEnd
                        )
                    }
            ) {
                CharacterCard(character) {
                    navController.navigate(navigateTo(character))
                }
            }
        }
    }
}






