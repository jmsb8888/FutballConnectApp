package com.task.futballconnectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.futballconnectapp.ui.AppHeader
import com.task.futballconnectapp.ui.FootballPostsScreen
import com.task.futballconnectapp.ui.Post
import com.task.futballconnectapp.ui.TeamInfo

val matchResults = listOf(
    com.task.futballconnectapp.ui.MatchResult(
        TeamInfo(1, "Team A", "urlA"),
        TeamInfo(2, "Team B", "urlB"),
        2,
        1
    ),
    com.task.futballconnectapp.ui.MatchResult(
        TeamInfo(3, "Team C", "urlC"),
        TeamInfo(4, "Team D", "urlD"),
        0,
        3
    ),
    com.task.futballconnectapp.ui.MatchResult(
        TeamInfo(5, "Team E", "urlE"),
        TeamInfo(6, "Team F", "urlF"),
        1,
        1
    ),
    com.task.futballconnectapp.ui.MatchResult(
        TeamInfo(7, "Team G", "urlG"),
        TeamInfo(8, "Team H", "urlH"),
        3,
        2
    )
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                MyApp()
        }
    }
}
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
                                isLiked = index % 2 == 0
                            )
                        }
                    }
                    FootballPostsScreen(posts = posts)
                }
            }
        }
    )
}
