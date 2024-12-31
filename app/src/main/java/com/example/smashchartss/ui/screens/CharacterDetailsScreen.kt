import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smashchartss.data.models.Changes
import com.example.smashchartss.data.models.Character
import com.example.smashchartss.data.remote.fetchChangesByCharacterId
import com.example.smashchartss.data.remote.fetchCharacters
import com.example.smashchartss.ui.theme.FontTittle

// Pantalla de Detalles del Personaje
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(characterId: String, navHostController: NavHostController) {
    val allCharacters = remember { mutableStateOf<List<Character>>(emptyList()) }
    val characterChanges = remember { mutableStateOf<List<Changes>>(emptyList()) }
    val currentCharacter = allCharacters.value.find { it.id == characterId }

    // Estado para controlar el zoom
    var scale by remember { mutableStateOf(1f) }

    // Lógica para obtener los datos del personaje y sus cambios
    LaunchedEffect(key1 = characterId) {
        try {
            // Fetch Characters
            val characters = fetchCharacters()
            allCharacters.value = characters

            // Fetch Changes
            val changes = fetchChangesByCharacterId(characterId)
            characterChanges.value = changes
        } catch (e: Exception) {
            Log.e("CharacterDetailsScreen", "Error fetching data: ${e.message}")
        }
    }

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
                            text = (currentCharacter?.name + " Changes") ?: "Unknown Character",
                            style = TextStyle(
                                fontFamily = FontTittle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            )
        }
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            // Modificar el zoom, con límites entre 0.5 y 2
                            scale = (scale * zoom).coerceIn(0.5f, 2f)
                        }
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount > 50) {
                                navHostController.navigateUp()
                            }
                        }
                    },
            ) {
                if (currentCharacter == null) {
                    Text(
                        text = "Character not found.",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    if (characterChanges.value.isEmpty()) {
                        Text(
                            text = "No changes available for this character.",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Light
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(characterChanges.value) { change ->
                                ChangeItem(change = change, scale = scale)
                            }
                        }
                    }
                }
            }

            // Indicador del Zoom (barra o texto)
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Zoom: ${(scale * 100).toInt()}%",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Indicador visual del rango de zoom
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(10.dp)
                        .background(Color.Gray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(Color.Green)
                            .height((scale * 200).dp) // El tamaño de la barra cambia con el zoom
                    )
                }
            }
        }
    }
}

// Componente para mostrar los cambios de un personaje
@Composable
fun ChangeItem(change: Changes, scale: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp)
    ) {
        // Mostrar el texto del cambio de manera más legible con zoom
        Text(
            text = change.text,
            style = TextStyle(
                fontSize = (16 * scale).sp, // Aplique el zoom al tamaño del texto
                fontWeight = FontWeight.Normal,
                color = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}
