package com.task.futballconnectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.task.futballconnectapp.ui.Coach
import com.task.futballconnectapp.ui.Competition
import com.task.futballconnectapp.ui.CompetitionScreen
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
import com.task.futballconnectapp.ui.MyApplicationTheme
import com.task.futballconnectapp.ui.Player
import com.task.futballconnectapp.ui.Team
import com.task.futballconnectapp.ui.UserRegistrationScreen

val competitions = listOf(
    Competition(
        id = 1,
        name = "Premier League",
        emblem = "https://crests.football-data.org/PL.png",
        teams = listOf(
            Team(
                id = 101,
                name = "Arsenal",
                shortName = "ARS",
                crest = "https://crests.football-data.org/57.png",
                venue = "Emirates Stadium",
                clubColors = "Red / White",
                coach = Coach(
                    id = 1001,
                    name = "Mikel Arteta",
                    dateOfBirth = "1982-03-26",
                    nationality = "Spanish"
                ),
                squad = listOf(
                    Player(id = 201, name = "Bukayo Saka", position = "Forward", dateOfBirth = "2001-09-05", nationality = "English"),
                    Player(id = 202, name = "Martin Ødegaard", position = "Midfielder", dateOfBirth = "1998-12-17", nationality = "Norwegian")
                ),
            ),
            Team(
                id = 102,
                name = "Chelsea",
                shortName = "CHE",
                crest = "https://crests.football-data.org/61.png",
                venue = "Stamford Bridge",
                clubColors = "Blue",
                coach = Coach(
                    id = 1002,
                    name = "Graham Potter",
                    dateOfBirth = "1975-05-20",
                    nationality = "English"
                ),
                squad = listOf(
                    Player(id = 203, name = "Mason Mount", position = "Midfielder", dateOfBirth = "1999-01-10", nationality = "English"),
                    Player(id = 204, name = "Raheem Sterling", position = "Forward", dateOfBirth = "1994-12-08", nationality = "English")
                ),
            )
        )
    ),
    Competition(
        id = 2,
        name = "La Liga",
        emblem = "https://crests.football-data.org/LL.png",
        teams = listOf(
            Team(
                id = 201,
                name = "Real Madrid",
                shortName = "RM",
                crest = "https://crests.football-data.org/RM.png",
                venue = "Santiago Bernabéu",
                clubColors = "White",
                coach = Coach(
                    id = 2001,
                    name = "Carlo Ancelotti",
                    dateOfBirth = "1959-06-10",
                    nationality = "Italian"
                ),
                squad = listOf(
                    Player(id = 301, name = "Karim Benzema", position = "Forward", dateOfBirth = "1987-12-19", nationality = "French"),
                    Player(id = 302, name = "Luka Modrić", position = "Midfielder", dateOfBirth = "1985-09-09", nationality = "Croatian")
                ),
            ),
            Team(
                id = 202,
                name = "FC Barcelona",
                shortName = "BAR",
                crest = "https://crests.football-data.org/BAR.png",
                venue = "Camp Nou",
                clubColors = "Blue / Red",
                coach = Coach(
                    id = 2002,
                    name = "Xavi Hernández",
                    dateOfBirth = "1980-01-25",
                    nationality = "Spanish"
                ),
                squad = listOf(
                    Player(id = 303, name = "Robert Lewandowski", position = "Forward", dateOfBirth = "1988-08-21", nationality = "Polish"),
                    Player(id = 304, name = "Frenkie de Jong", position = "Midfielder", dateOfBirth = "1997-05-12", nationality = "Dutch")
                ),
            )
        )
    ),
    Competition(
        id = 3,
        name = "Serie A",
        emblem = "https://crests.football-data.org/SA.png",
        teams = listOf(
            Team(
                id = 301,
                name = "Juventus",
                shortName = "JUV",
                crest = "https://crests.football-data.org/JUV.png",
                venue = "Allianz Stadium",
                clubColors = "Black / White",
                coach = Coach(
                    id = 3001,
                    name = "Massimiliano Allegri",
                    dateOfBirth = "1967-08-11",
                    nationality = "Italian"
                ),
                squad = listOf(
                    Player(id = 401, name = "Cristiano Ronaldo", position = "Forward", dateOfBirth = "1985-02-05", nationality = "Portuguese"),
                    Player(id = 402, name = "Paulo Dybala", position = "Forward", dateOfBirth = "1993-11-15", nationality = "Argentine")
                ),
            ),
            Team(
                id = 302,
                name = "AC Milan",
                shortName = "MIL",
                crest = "https://crests.football-data.org/MIL.png",
                venue = "San Siro",
                clubColors = "Red / Black",
                coach = Coach(
                    id = 3002,
                    name = "Stefano Pioli",
                    dateOfBirth = "1965-10-20",
                    nationality = "Italian"
                ),
                squad = listOf(
                    Player(id = 403, name = "Zlatan Ibrahimović", position = "Forward", dateOfBirth = "1981-10-03", nationality = "Swedish"),
                    Player(id = 404, name = "Theo Hernández", position = "Defender", dateOfBirth = "1997-10-06", nationality = "French")
                ),
            )
        )
    ),
    Competition(
        id = 4,
        name = "Bundesliga",
        emblem = "https://crests.football-data.org/BL.png",
        teams = listOf(
            Team(
                id = 401,
                name = "Bayern Munich",
                shortName = "BAY",
                crest = "https://crests.football-data.org/BAY.png",
                venue = "Allianz Arena",
                clubColors = "Red",
                coach = Coach(
                    id = 4001,
                    name = "Julian Nagelsmann",
                    dateOfBirth = "1987-07-23",
                    nationality = "German"
                ),
                squad = listOf(
                    Player(id = 501, name = "Robert Lewandowski", position = "Forward", dateOfBirth = "1988-08-21", nationality = "Polish"),
                    Player(id = 502, name = "Joshua Kimmich", position = "Midfielder", dateOfBirth = "1995-02-08", nationality = "German")
                ),
            ),
            Team(
                id = 402,
                name = "Borussia Dortmund",
                shortName = "BVB",
                crest = "https://crests.football-data.org/BVB.png",
                venue = "Signal Iduna Park",
                clubColors = "Yellow / Black",
                coach = Coach(
                    id = 4002,
                    name = "Edin Terzić",
                    dateOfBirth = "1982-10-30",
                    nationality = "Bosnian"
                ),
                squad = listOf(
                    Player(id = 503, name = "Erling Haaland", position = "Forward", dateOfBirth = "2000-07-21", nationality = "Norwegian"),
                    Player(id = 504, name = "Jude Bellingham", position = "Midfielder", dateOfBirth = "2003-06-29", nationality = "English")
                ),
            )
        )
    ),
    Competition(
        id = 5,
        name = "Ligue 1",
        emblem = "https://crests.football-data.org/L1.png",
        teams = listOf(
            Team(
                id = 501,
                name = "Paris Saint-Germain",
                shortName = "PSG",
                crest = "https://crests.football-data.org/PSG.png",
                venue = "Parc des Princes",
                clubColors = "Blue / Red",
                coach = Coach(
                    id = 5001,
                    name = "Christophe Galtier",
                    dateOfBirth = "1966-08-23",
                    nationality = "French"
                ),
                squad = listOf(
                    Player(id = 601, name = "Lionel Messi", position = "Forward", dateOfBirth = "1987-06-24", nationality = "Argentine"),
                    Player(id = 602, name = "Neymar", position = "Forward", dateOfBirth = "1992-02-05", nationality = "Brazilian")
                ),
            ),
            Team(
                id = 502,
                name = "Olympique Lyonnais",
                shortName = "OL",
                crest = "https://crests.football-data.org/OL.png",
                venue = "Groupama Stadium",
                clubColors = "Red / Blue",
                coach = Coach(
                    id = 5002,
                    name = "Laurent Blanc",
                    dateOfBirth = "1965-11-19",
                    nationality = "French"
                ),
                squad = listOf(
                    Player(id = 603, name = "Alexandre Lacazette", position = "Forward", dateOfBirth = "1991-05-28", nationality = "French"),
                    Player(id = 604, name = "Houssem Aouar", position = "Midfielder", dateOfBirth = "1998-06-30", nationality = "French")
                ),
            )
        )
    ),
    Competition(
        id = 6,
        name = "Eredivisie",
        emblem = "https://crests.football-data.org/ED.png",
        teams = listOf(
            Team(
                id = 601,
                name = "Ajax",
                shortName = "AJA",
                crest = "https://crests.football-data.org/AJA.png",
                venue = "Johan Cruijff Arena",
                clubColors = "Red / White",
                coach = Coach(
                    id = 6001,
                    name = "John Heitinga",
                    dateOfBirth = "1983-11-09",
                    nationality = "Dutch"
                ),
                squad = listOf(
                    Player(id = 701, name = "Dusan Tadić", position = "Forward", dateOfBirth = "1988-11-20", nationality = "Serbian"),
                    Player(id = 702, name = "Antony", position = "Forward", dateOfBirth = "2000-02-24", nationality = "Brazilian")
                ),
            ),
            Team(
                id = 602,
                name = "PSV Eindhoven",
                shortName = "PSV",
                crest = "https://crests.football-data.org/PSV.png",
                venue = "Philips Stadion",
                clubColors = "Red / White",
                coach = Coach(
                    id = 6002,
                    name = "Ruud van Nistelrooy",
                    dateOfBirth = "1976-07-01",
                    nationality = "Dutch"
                ),
                squad = listOf(
                    Player(id = 703, name = "Cody Gakpo", position = "Forward", dateOfBirth = "1999-05-07", nationality = "Dutch"),
                    Player(id = 704, name = "Joey Veerman", position = "Midfielder", dateOfBirth = "1998-01-12", nationality = "Dutch")
                ),
            )
        )
    ),
    Competition(
        id = 7,
        name = "MLS",
        emblem = "https://crests.football-data.org/MLS.png",
        teams = listOf(
            Team(
                id = 701,
                name = "LA Galaxy",
                shortName = "LA",
                crest = "https://crests.football-data.org/LA.png",
                venue = "Dignity Health Sports Park",
                clubColors = "Gold / Blue",
                coach = Coach(
                    id = 7001,
                    name = "Greg Vanney",
                    dateOfBirth = "1974-01-19",
                    nationality = "American"
                ),
                squad = listOf(
                    Player(id = 801, name = "Chicharito Hernández", position = "Forward", dateOfBirth = "1988-06-01", nationality = "Mexican"),
                    Player(id = 802, name = "Riqui Puig", position = "Midfielder", dateOfBirth = "1999-08-13", nationality = "Spanish")
                ),
            ),
            Team(
                id = 702,
                name = "New York City FC",
                shortName = "NYC",
                crest = "https://crests.football-data.org/NYC.png",
                venue = "Yankee Stadium",
                clubColors = "Blue / White",
                coach = Coach(
                    id = 7002,
                    name = "Nick Cushing",
                    dateOfBirth = "1985-01-05",
                    nationality = "English"
                ),
                squad = listOf(
                    Player(id = 803, name = "Talles Magno", position = "Forward", dateOfBirth = "2002-06-26", nationality = "Brazilian"),
                    Player(id = 804, name = "Santiago Rodríguez", position = "Midfielder", dateOfBirth = "2000-06-21", nationality = "Uruguayan")
                ),
            )
        )
    ),
    Competition(
        id = 8,
        name = "Portuguese Primeira Liga",
        emblem = "https://crests.football-data.org/PLG.png",
        teams = listOf(
            Team(
                id = 801,
                name = "FC Porto",
                shortName = "FCP",
                crest = "https://crests.football-data.org/FCP.png",
                venue = "Estádio do Dragão",
                clubColors = "Blue / White",
                coach = Coach(
                    id = 8001,
                    name = "Sérgio Conceição",
                    dateOfBirth = "1974-11-15",
                    nationality = "Portuguese"
                ),
                squad = listOf(
                    Player(id = 901, name = "Pepe", position = "Defender", dateOfBirth = "1983-02-26", nationality = "Portuguese"),
                    Player(id = 902, name = "Luis Díaz", position = "Forward", dateOfBirth = "1996-01-13", nationality = "Colombian")
                ),
            ),
            Team(
                id = 802,
                name = "Benfica",
                shortName = "BEN",
                crest = "https://crests.football-data.org/BEN.png",
                venue = "Estádio da Luz",
                clubColors = "Red / White",
                coach = Coach(
                    id = 8002,
                    name = "Roger Schmidt",
                    dateOfBirth = "1967-03-13",
                    nationality = "German"
                ),
                squad = listOf(
                    Player(id = 903, name = "Darwin Núñez", position = "Forward", dateOfBirth = "1999-06-24", nationality = "Uruguayan"),
                    Player(id = 904, name = "Ruben Dias", position = "Defender", dateOfBirth = "1997-05-14", nationality = "Portuguese")
                ),
            )
        )
    ),
    Competition(
        id = 9,
        name = "Brasileirão",
        emblem = "https://crests.football-data.org/BR.png",
        teams = listOf(
            Team(
                id = 901,
                name = "Flamengo",
                shortName = "FLA",
                crest = "https://crests.football-data.org/FLA.png",
                venue = "Maracanã",
                clubColors = "Red / Black",
                coach = Coach(
                    id = 9001,
                    name = "Jorge Sampaoli",
                    dateOfBirth = "1960-03-13",
                    nationality = "Argentine"
                ),
                squad = listOf(
                    Player(id = 1001, name = "Gabigol", position = "Forward", dateOfBirth = "1996-08-30", nationality = "Brazilian"),
                    Player(id = 1002, name = "Arrascaeta", position = "Midfielder", dateOfBirth = "1994-06-30", nationality = "Uruguayan")
                ),
            ),
            Team(
                id = 902,
                name = "Palmeiras",
                shortName = "PAL",
                crest = "https://crests.football-data.org/PAL.png",
                venue = "Allianz Parque",
                clubColors = "Green / White",
                coach = Coach(
                    id = 9002,
                    name = "Abel Ferreira",
                    dateOfBirth = "1978-10-24",
                    nationality = "Portuguese"
                ),
                squad = listOf(
                    Player(id = 1003, name = "Dudu", position = "Forward", dateOfBirth = "1992-01-07", nationality = "Brazilian"),
                    Player(id = 1004, name = "Gustavo Scarpa", position = "Midfielder", dateOfBirth = "1994-03-05", nationality = "Brazilian")
                ),
            )
        )
    ),
    Competition(
        id = 10,
        name = "Argentina Primera División",
        emblem = "https://crests.football-data.org/AR.png",
        teams = listOf(
            Team(
                id = 1001,
                name = "Boca Juniors",
                shortName = "BOC",
                crest = "https://crests.football-data.org/BOC.png",
                venue = "La Bombonera",
                clubColors = "Blue / Yellow",
                coach = Coach(
                    id = 10001,
                    name = "Hugo Ibarra",
                    dateOfBirth = "1974-02-01",
                    nationality = "Argentine"
                ),
                squad = listOf(
                    Player(id = 1101, name = "Carlos Tévez", position = "Forward", dateOfBirth = "1984-02-05", nationality = "Argentine"),
                    Player(id = 1102, name = "Darío Benedetto", position = "Forward", dateOfBirth = "1990-05-17", nationality = "Argentine")
                ),
            ),
            Team(
                id = 1002,
                name = "River Plate",
                shortName = "RIV",
                crest = "https://crests.football-data.org/RIV.png",
                venue = "Monumental Stadium",
                clubColors = "Red / White",
                coach = Coach(
                    id = 10002,
                    name = "Marcelo Gallardo",
                    dateOfBirth = "1976-01-18",
                    nationality = "Argentine"
                ),
                squad = listOf(
                    Player(id = 1103, name = "Javier Pinola", position = "Defender", dateOfBirth = "1983-01-18", nationality = "Argentine"),
                    Player(id = 1104, name = "Matías Suárez", position = "Forward", dateOfBirth = "1988-05-08", nationality = "Argentine")
                ),
            )
        )
    )
)


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
            MyApplicationTheme {
                CompetitionScreen(
                    competitions = competitions,
                    onTeamSelected = {
                    }
                )
            }
        }
    }
}

