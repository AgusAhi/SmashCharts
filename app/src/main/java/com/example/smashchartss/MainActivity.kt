package com.example.smashchartss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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

/*
todo poner el nombre del personaje en la topAPPBar
todo funcionalidad del botón de compartir
todo funcionalidad del botón de settings con la info del programa
todo apartado de información de cada personaje por IA
todo character info page donde veremos los cambios de balance de cada uno y la info por ia que es lo mismo que lo de arriba pero en otra pantalla
*/

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

        // Menu Screen

        composable(
            route = "menuScreen/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            MenuScreen(characterId = characterId, navHostController = navController)
        }

        // Matchup Chart

        composable(
            route = "matchupChart/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            MatchupChart(characterId = characterId, navHostController = navController)
        }

    }
}




