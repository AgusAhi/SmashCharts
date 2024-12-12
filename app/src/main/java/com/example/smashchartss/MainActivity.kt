package com.example.smashchartss

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smashchartss.ui.theme.SmashChartssTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(35)
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
FUTURO: todo que en matchupChart puedas dirigirte a cada personaje con el botón individualmente
todo funcionalidad del botón de compartir
todo funcionalidad del botón de settings con la info del programa
todo bug de que se abre el teclado
*/

@RequiresApi(35)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "MainSelectScreen"
    ) {
        // Pantalla principal
        composable("MainSelectScreen") {
            MainSelectScreen(navController = navController)
        }

        // Pantalla del menú de un personaje
        composable(
            route = "menuScreen/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            if (!characterId.isNullOrEmpty()) {
                MenuScreen(characterId = characterId, navHostController = navController)
            } else {
                Log.e("AppNavigator", "characterId is null or empty in menuScreen route.")
            }
        }

        // Pantalla de la tabla de enfrentamientos
        composable(
            route = "matchupChart/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            if (!characterId.isNullOrEmpty()) {
                MatchupChart(characterId = characterId, navHostController = navController)
            } else {
                Log.e("AppNavigator", "characterId is null or empty in matchupChart route.")
            }
        }

        // Pantalla de detalles del personaje
        composable(
            route = "characterDetail/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            if (!characterId.isNullOrEmpty()) {
                CharacterDetailsScreen(characterId = characterId, navHostController = navController)
            } else {
                Log.e("AppNavigator", "characterId is null or empty in characterDetail route.")
            }
        }
    }
}





