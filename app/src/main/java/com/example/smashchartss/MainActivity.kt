package com.example.smashchartss

import ChatBotScreen
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
FUTURO funcionalidad del botón de compartir para que me permita exportar una imagen como te guste
FUTURO funcionalidad del botón de settings, perfil e info
FUTURO: darle formato al texto para representar distintas secciones (como cambios en la versión o detalles de los ajustes),
podría usar técnicas de parsing para separar el texto y crear componentes adicionales.
FUTURO: MAS ANIMACIONES
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


        // Pantalla de chatbot
        composable("chatBot") {
            ChatBotScreen(navHostController = navController)
        }

        // Pantalla de info

        composable("appInfo") { AppInfoScreen() } // Añade esta línea para la nueva clase


    }
}











