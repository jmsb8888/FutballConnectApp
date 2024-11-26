package com.task.futballconnectapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.futballconnectapp.matchResults

@Composable
fun MyApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { AppHeader() },
        content = { padding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") {
                    val posts = remember {
                        matchResults.mapIndexed { index, matchResult ->
                            Post(
                                userName = "Usuario $index",
                                userProfileImageUrl = "https://crests.football-data.org/PL.png",
                                mainImageUrl = "https://crests.football-data.org/PL.png",
                                title = "Resumen del partido ${matchResult.homeTeam.shortName} vs ${matchResult.awayTeam.shortName}",
                                description = "Un emocionante enfrentamiento entre ${matchResult.homeTeam.shortName} y ${matchResult.awayTeam.shortName}.",
                                matchResult = matchResult,
                                isLiked = index % 2 == 0,
                                comments = listOf(
                                    Comment(userName = "Fan $index", text = "¡Qué partido tan emocionante!"),
                                    Comment(userName = "Analista $index", text = "La estrategia del equipo local fue impresionante."),
                                    Comment(userName = "Aficionado $index", text = "El gol del minuto 90 fue increíble.")
                                )
                            )
                        }
                    }
                    FootballPostsScreen(posts = posts)
                }
            }
        }
    )
}
