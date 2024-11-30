package com.task.futballconnectapp.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.task.futballconnectapp.data.api.models.Coach
import com.task.futballconnectapp.data.api.models.CompetitionD
import com.task.futballconnectapp.data.api.models.Player
import com.task.futballconnectapp.data.api.models.Team
import com.task.futballconnectapp.data.viewmodel.ApiViewModel


data class Competition(
    val id: Int,
    val name: String,
    val emblem: String,
    val teams: List<Team>
)




@Composable
fun CompetitionScreen(
    onTeamSelected: (Team) -> Unit,
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    var selectedCompetition by remember { mutableStateOf<CompetitionD?>(null) }
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var selectedCoach by remember { mutableStateOf<Coach?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current
    val competitionsState = apiViewModel.competitions.collectAsState().value
    val teamsState = apiViewModel.teams.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            PostInputFields(
                title = title,
                description = description,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it }
            )
            if (competitionsState.isLoading) {
                CircularProgressIndicator() // Indicador de carga mientras se obtienen los datos
            } else if (competitionsState.competitions != null) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(competitionsState.competitions) { competition ->
                        CompetitionItem(
                            competition = competition,
                            isSelected = selectedCompetition == competition,
                            onClick = {
                                apiViewModel.fetchAndExtractTeams(competition.id)
                                if (selectedCompetition == competition) {
                                    selectedCompetition = null
                                } else {
                                    selectedCompetition = competition
                                    selectedTeam = null
                                    selectedPlayer = null
                                    selectedCoach = null
                                }
                            }
                        )
                    }
                }
            }
            if (teamsState.isLoading && selectedCompetition != null) {
                CircularProgressIndicator() // Indicador de carga mientras se obtienen los datos
            } else if (teamsState.teams != null && selectedCompetition != null) {
                TeamsSection(
                    teams = teamsState.teams,
                    selectedTeam = selectedTeam,
                    onTeamClick = {
                        if (selectedTeam == it) {
                            selectedTeam = null
                        } else {
                            selectedTeam = it
                            selectedPlayer = null
                            selectedCoach = null
                        }
                    }
                )

                selectedTeam?.let { team ->
                    TeamDetails(
                        team = team,
                        selectedPlayer = selectedPlayer,
                        selectedCoach = selectedCoach,
                        onPlayerClick = { player ->
                            selectedPlayer = if (selectedPlayer == player) null else player
                            selectedCoach = null
                        },
                        onCoachClick = {
                            selectedCoach =
                                if (selectedCoach == team.coach) null else team.coach
                            selectedPlayer = null
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                handlePostCreation(
                    context = context,
                    title = title,
                    description = description,
                    selectedPlayer = selectedPlayer,
                    selectedCoach = selectedCoach,
                    navController = navController
                )

            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF4CAF50)
        ) {
            Text("Publicar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostInputFields(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = inputFieldColors(),
        )
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = inputFieldColors(),

            )
    }
}

@Composable
fun TeamsSection(
    teams: List<Team>,
    selectedTeam: Team?,
    onTeamClick: (Team) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Equipos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(teams) { team ->
                TeamCircleItem(
                    team = team,
                    isSelected = selectedTeam == team,
                    onClick = { onTeamClick(team) },
                    backgroundColor = if (selectedTeam == team) Color(0xFF4CAF50).copy(alpha = 0.5f) else Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun TeamDetails(
    team: Team,
    selectedPlayer: Player?,
    selectedCoach: Coach?,
    onPlayerClick: (Player) -> Unit,
    onCoachClick: () -> Unit
) {
    // Lista que contiene tanto el entrenador como los jugadores
    val members = listOf(
        team.coach,
        *team.squad.toTypedArray()
    )

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(members) { member ->
            when (member) {
                is Coach -> {
                    // Mostrar los detalles del entrenador
                    Text("Entrenador Y Juegadores:", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(
                                if (selectedCoach == member) Color.Green.copy(alpha = 0.3f) else Color.Gray.copy(
                                    alpha = 0.3f
                                )
                            )
                            .clickable {
                                onCoachClick()
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nombre: ${member.name}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedCoach == member) Color.Green else Color.White
                            )
                            Text(
                                text = "Fecha de nacimiento: ${member.dateOfBirth}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedCoach == member) Color.Green else Color.White
                            )
                            Text(
                                text = "Nacionalidad: ${member.nationality}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedCoach == member) Color.Green else Color.White
                            )
                        }
                    }
                }
                is Player -> {
                    // Mostrar los detalles del jugador
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(
                                if (selectedPlayer == member) Color.Green.copy(alpha = 0.3f) else Color.Gray.copy(
                                    alpha = 0.3f
                                )
                            )
                            .clickable {
                                onPlayerClick(member)
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nombre: ${member.name}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedPlayer == member) Color.Green else Color.White
                            )
                            Text(
                                text = "Posición: ${member.position}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedPlayer == member) Color.Green else Color.White
                            )
                            Text(
                                text = "Fecha de nacimiento: ${member.dateOfBirth}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedPlayer == member) Color.Green else Color.White
                            )
                            Text(
                                text = "Nacionalidad: ${member.nationality}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedPlayer == member) Color.Green else Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


// Sellado que define los elementos que se pueden mostrar: Coach o Player
sealed class Member {
    data class Coach(val coach: Coach) : Member()
    data class Player(val player: Player) : Member()
}


fun handlePostCreation(
    context: Context,
    title: String,
    description: String,
    selectedPlayer: Player?,
    selectedCoach: Coach?,
    navController: NavController
) {
    when {
        title.isEmpty() || description.isEmpty() ->
            Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                .show()

        selectedPlayer == null && selectedCoach == null ->
            Toast.makeText(context, "Seleccione un jugador o entrenador", Toast.LENGTH_SHORT).show()

        else -> {
            Toast.makeText(context, "Publicación creada: $title", Toast.LENGTH_SHORT).show()
            navController.navigate("results")
        }
    }
}


@Composable
fun TeamCircleItem(
    team: Team,
    isSelected: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color = Color.Red
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(team.crest),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        }
        Text(
            text = team.name,
            color = if (isSelected) Color.Green else Color.White,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun CompetitionItem(
    competition: CompetitionD,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val customSurfaceColor = Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onClick() }
            .background(Color.Transparent)
            .padding(1.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Green else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(competition.emblem),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .background(customSurfaceColor)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = competition.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isSelected) Color.Green else Color.White
        )
    }
}
