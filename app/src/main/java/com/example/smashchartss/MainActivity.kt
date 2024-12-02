package com.example.smashchartss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        CharacterViewModel(CharacterRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigator(viewModel = viewModel)
        }
    }
}

@Composable
fun AppNavigator(viewModel: CharacterViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.characters.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "characterList"
    ) {
        composable("characterList") {
            when (uiState) {
                is CharacterListUiState.Loading -> LoadingScreen()
                is CharacterListUiState.Success -> {
                    val characterList = (uiState as CharacterListUiState.Success).characters
                    CharacterList(
                        characters = characterList,
                        navController = navController
                    )
                }
                is CharacterListUiState.Error -> {
                    val message = (uiState as CharacterListUiState.Error).message
                    ErrorScreen(message = message)
                }
            }
        }

        composable(
            route = "menuScreen/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            MenuScreen(characterId = characterId, navHostController = navController)
        }

        composable(
            route = "matchupChart/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
            if (uiState is CharacterListUiState.Success) {
                val characters = (uiState as CharacterListUiState.Success).characters
                MatchupChart(characters = characters)
            } else {
                LoadingScreen()
            }
        }
    }
}



