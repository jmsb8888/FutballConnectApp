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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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


data class Competition(
    val id: Int,
    val name: String,
    val emblem: String,
    val teams: List<Team>
)

data class Team(
    val id: Int,
    val name: String,
    val shortName: String,
    val crest: String,
    val venue: String,
    val clubColors: String,
    val coach: Coach,
    val squad: List<Player>,
    //val competition: Competition
)

data class Coach(
    val id: Int,
    val name: String,
    val dateOfBirth: String,
    val nationality: String,
)

data class Player(
    val id: Int,
    val name: String,
    val position: String,
    val dateOfBirth: String,
    val nationality: String
)

@Composable
fun CompetitionScreen(
    competitions: List<Competition>,
    onTeamSelected: (Team) -> Unit
) {
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var selectedCoach by remember { mutableStateOf<Coach?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            PostInputFields(
                title = title,
                description = description,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it }
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(competitions) { competition ->
                    CompetitionItem(
                        competition = competition,
                        isSelected = selectedCompetition == competition,
                        onClick = {
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

            selectedCompetition?.let { competition ->
                TeamsSection(
                    teams = competition.teams,
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
            }
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
                        selectedCoach = if (selectedCoach == team.coach) null else team.coach
                        selectedPlayer = null
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = {
                handlePostCreation(
                    context = context,
                    title = title,
                    description = description,
                    selectedPlayer = selectedPlayer,
                    selectedCoach = selectedCoach
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF4CAF50)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Publicar")
        }
    }
}

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
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            colors = inputFieldColors(),
        )
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
    Card(modifier = Modifier.padding(top = 16.dp).fillMaxWidth()) {
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
    Spacer(modifier = Modifier.height(16.dp))

    Text("Entrenador:", style = MaterialTheme.typography.titleLarge, color = Color.White)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(
                if (selectedCoach == team.coach) Color.Green.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f)
            )
            .clickable {
                onCoachClick()
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Nombre: ${team.coach.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedCoach == team.coach) Color.Green else Color.White
            )
            Text(
                text = "Fecha de nacimiento: ${team.coach.dateOfBirth}",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedCoach == team.coach) Color.Green else Color.White
            )
            Text(
                text = "Nacionalidad: ${team.coach.nationality}",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedCoach == team.coach) Color.Green else Color.White
            )
        }
    }

    Text("Jugadores:", style = MaterialTheme.typography.titleLarge, color = Color.White)
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(team.squad) { player ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(
                        if (selectedPlayer == player) Color.Green.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f)
                    )
                    .clickable {
                        onPlayerClick(player)
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nombre: ${player.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedPlayer == player) Color.Green else Color.White
                    )
                    Text(
                        text = "Posición: ${player.position}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedPlayer == player) Color.Green else Color.White
                    )
                    Text(
                        text = "Fecha de nacimiento: ${player.dateOfBirth}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedPlayer == player) Color.Green else Color.White
                    )
                    Text(
                        text = "Nacionalidad: ${player.nationality}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedPlayer == player) Color.Green else Color.White
                    )
                }
            }
        }
    }
}



fun handlePostCreation(
    context: Context,
    title: String,
    description: String,
    selectedPlayer: Player?,
    selectedCoach: Coach?
) {
    when {
        title.isEmpty() || description.isEmpty() ->
            Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
        selectedPlayer == null && selectedCoach == null ->
            Toast.makeText(context, "Seleccione un jugador o entrenador", Toast.LENGTH_SHORT).show()
        else ->
            Toast.makeText(context, "Publicación creada: $title", Toast.LENGTH_SHORT).show()
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
    competition: Competition,
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
