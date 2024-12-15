package com.example.smashchartss

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smashchartss.ui.theme.FontTittle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(characterId: String?, navHostController: NavHostController) {
    var isMenuOpen by remember { mutableStateOf(false) }
    var settingsIconRotation by remember { mutableStateOf(0f) }

    val animatedRotation by animateFloatAsState(targetValue = settingsIconRotation)

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
                            text = "Smash O Charts",
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
                    // Botón de settings con animación
                    IconButton(onClick = {
                        isMenuOpen = !isMenuOpen
                        settingsIconRotation += 360f
                    }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier.rotate(animatedRotation)
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuOpen,
                        onDismissRequest = { isMenuOpen = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface) // Opcional: añade un fondo
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                navHostController.navigate("settings")
                                isMenuOpen = false // Cierra el menú después de navegar
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Settings, contentDescription = "Settings") // Opcional: añade un ícono
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Profile") },
                            onClick = {
                                navHostController.navigate("profile")
                                isMenuOpen = false // Cierra el menú después de navegar
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = "Perfil") // Opcional: añade un ícono
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Info") },
                            onClick = {
                                navHostController.navigate("appInfo")
                                isMenuOpen = false // Cierra el menú después de navegar
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Info, contentDescription = "App Info") // Opcional: añade un ícono
                            }
                        )
                    }
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
            // Título
            Text(
                text = "MOde Menu",
                style = TextStyle(
                    fontFamily = FontTittle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 45.sp,
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navHostController.navigate("characterDetail/$characterId")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            ) {
                Text("Character Info")
            }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navHostController.navigate("chatBot")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            ) {
                Text("ChatBot")
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Botón para función futura
            Button(
                onClick = {
                    // Acción para la función "Coming soon"
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            ) {
                Text("Coming soon...")
            }
        }
    }
}
