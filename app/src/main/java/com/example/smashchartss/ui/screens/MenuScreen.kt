package com.example.smashchartss


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                navHostController.navigate("settings")
                                isMenuOpen = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Settings, contentDescription = "Settings")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Profile") },
                            onClick = {
                                navHostController.navigate("profile")
                                isMenuOpen = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = "Profile")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Info") },
                            onClick = {
                                navHostController.navigate("appInfo")
                                isMenuOpen = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Info, contentDescription = "App Info")
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isLandscape = maxWidth > maxHeight


            if (isLandscape) {
                ScrollableMenuContent(characterId, navHostController, padding, isLandscape = true)
            } else {
                ScrollableMenuContent(characterId, navHostController, padding, isLandscape = false)
            }
        }
    }
}


@Composable
fun ScrollableMenuContent(characterId: String?, navHostController: NavHostController, padding: PaddingValues, isLandscape: Boolean) {
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally, // Centrado en ambas orientaciones
        verticalArrangement = if (isLandscape) Arrangement.Center else Arrangement.Top // Diferente disposición según orientación
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


        MenuButtons(characterId, navHostController)
    }
}


@Composable
fun MenuButtons(characterId: String?, navHostController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Botones centrados
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { navHostController.navigate("matchupChart/$characterId") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ) {
            Text("Matchup Chart")
        }


        Button(
            onClick = { navHostController.navigate("characterDetail/$characterId") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ) {
            Text("Character Info")
        }


        Button(
            onClick = { navHostController.navigate("chatBot") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ) {
            Text("ChatBot")
        }


        Button(
            onClick = { /* Acción futura */ },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ) {
            Text("Coming soon...")
        }
    }
}




