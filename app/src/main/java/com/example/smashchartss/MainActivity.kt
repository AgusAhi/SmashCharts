package com.example.smashchartss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smashchartss.ui.theme.SmashChartssTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmashChartssTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "MainSelectScreen"
    ) {
        composable("MainSelectScreen") {
            MainSelectScreen(navController = navController)
        }


        composable(
            route = "menuScreen/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            MenuScreen(characterId = characterId, navHostController = navController)
        }

        /*

        composable(
            route = "matchupChart/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->

         */



            /*
            if (uiState is CharacterListUiState.Success) {
                val characters = (uiState as CharacterListUiState.Success).characters
                MatchupChart(characters = characters)
            } else {
                LoadingScreen()
            }

             */


    }
}




