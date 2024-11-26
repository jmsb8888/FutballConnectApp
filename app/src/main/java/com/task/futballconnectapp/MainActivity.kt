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
import com.task.futballconnectapp.ui.MyApp
import com.task.futballconnectapp.ui.Post
import com.task.futballconnectapp.ui.PostPerson
import com.task.futballconnectapp.ui.TeamInfo

val matchResultstwo = listOf(
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
val matchResults = listOf(
    PostPerson(id = 1101, name = "Carlos Tévez", position = "Forward", dateOfBirth = "1984-02-05", nationality = "Argentine"),
    PostPerson(id = 1102, name = "Darío Benedetto", position = "Forward", dateOfBirth = "1990-05-17", nationality = "Argentine"),
    PostPerson(id = 1103, name = "Javier Pinola", position = "Defender", dateOfBirth = "1983-01-18", nationality = "Argentine"),
    PostPerson(id = 1104, name = "Matías Suárez", position = "Forward", dateOfBirth = "1988-05-08", nationality = "Argentine"),
    PostPerson(id = 1003, name = "Dudu", position = "Forward", dateOfBirth = "1992-01-07", nationality = "Brazilian"),
    PostPerson(id = 1004, name = "Gustavo Scarpa", dateOfBirth = "1994-03-05", nationality = "Brazilian")
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                //MyApp()

                val posts = remember {
                    matchResults.mapIndexed { index, matchResult ->
                        Post(
                            userName = "Usuario $index",
                            userProfileImageUrl = "https://crests.football-data.org/PL.png",
                            mainImageUrl = "https://crests.football-data.org/PL.png",
                            title = "Resumen del partido",
                            description = "Un emocionante enfrentamiento entre",
                            matchResult = null,
                            person = matchResult,
                            isLiked = index % 2 == 0
                        )
                    }
                }
                FootballPostsScreen(posts = posts)
        }
    }
}
