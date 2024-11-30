package com.task.futballconnectapp.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.task.futballconnectapp.data.api.models.CompetitionD
import com.task.futballconnectapp.data.api.models.ResultsD
import com.task.futballconnectapp.data.api.models.Team
import com.task.futballconnectapp.data.api.models.TeamInfo
import com.task.futballconnectapp.data.viewmodel.ApiViewModel


data class MatchResult(
    val homeTeam: TeamInfo,
    val awayTeam: TeamInfo,
    val fullTimeScoreHome: Int,
    val fullTimeScoreAway: Int
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(navController: NavController,  apiViewModel: ApiViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val competitionsState = apiViewModel.competitions.collectAsState().value
    val matchResults = apiViewModel.results.collectAsState().value // Observa los resultados desde el ViewModel
    val context = LocalContext.current
    var selectedMatch by remember { mutableStateOf<ResultsD?>(null) }
    var selectedCompetition by remember { mutableStateOf<CompetitionD?>(null) }
    fun handlePostCreation() {
        if (title.isNotEmpty() && description.isNotEmpty() && selectedMatch != null) {
            Toast.makeText(context, "Publicación creada: $title", Toast.LENGTH_SHORT).show()
            navController.navigate("home")
        } else {
            Toast.makeText(
                context,
                "Por favor, complete todos los campos y seleccione un partido",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Publicación",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título de la publicación") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = inputFieldColors()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción breve") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                colors = inputFieldColors()
            )
            if (competitionsState.isLoading) {
                CircularProgressIndicator() // Indicador de carga mientras se obtienen los datos
            } else if (competitionsState.competitions != null) {
                // Pasamos las competiciones obtenidas del ViewModel
                CompetitionList(
                    competitions = competitionsState.competitions,
                    selectedCompetition = selectedCompetition,
                    onCompetitionClick = {
                            competition ->
                        selectedCompetition = competition
                        apiViewModel.fetchAndExtractResults(competition.id)
                    }
                )
            }
            if (matchResults.isLoading && selectedMatch != null) {
                CircularProgressIndicator() // Indicador de carga mientras se obtienen los datos
            } else if (matchResults.results != null) {
                MatchResultList(
                    matchResults = matchResults.results,
                    selectedMatch = selectedMatch,
                    onMatchSelected = { match ->
                        selectedMatch = match
                    })
            }
        }
        FloatingActionButton(
            onClick = { handlePostCreation() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF4CAF50)
        ) {
            Text("Publicar")
        }
    }
}

@Composable
fun CompetitionItemRow(
    competition: CompetitionD,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val customSurfaceColor = Color.Gray

    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
            .width(100.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Green else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(competition.emblem),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .background(customSurfaceColor)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = competition.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = if (isSelected) Color.Green else Color.White
        )
    }
}
@Composable
fun TeamItemRow(
    team: Team,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val customSurfaceColor = Color.Gray

    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
            .width(100.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Green else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(team.crest),
            contentDescription = null,
            modifier = Modifier
                .size(75.dp)
                .clip(CircleShape)
                .background(customSurfaceColor)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = team.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = if (isSelected) Color.Green else Color.White
        )
    }
}
@Composable
fun CompetitionList(
    competitions: List<CompetitionD>,
    selectedCompetition: CompetitionD?,
    onCompetitionClick: (CompetitionD) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(competitions) { competition ->
            CompetitionItemRow(
                competition = competition,
                isSelected = competition == selectedCompetition,
                onClick = { onCompetitionClick(competition) }
            )
        }
    }
}

@Composable
fun teamsList(
    teams: List<Team>,
    selectedTeam: Team?,
    onTeamClick: (Team) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(teams) { team ->
            TeamItemRow(
                team = team,
                isSelected = team == selectedTeam,
                onClick = { onTeamClick(team) }
            )
        }
    }
}
@Composable
fun MatchResultList(
    matchResults: List<ResultsD>,
    selectedMatch: ResultsD?,
    onMatchSelected: (ResultsD) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(matchResults) { matchResult ->
            MatchResultCard(
                matchResult = matchResult,
                isSelected = selectedMatch == matchResult,
                onClick = { onMatchSelected(matchResult) }
            )
        }
    }
}

@Composable
fun MatchResultCard(
    matchResult: ResultsD,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
    val borderColor = if (isSelected) Color(0xFF4CAF50) else Color.Transparent
    val textColor = if (isSelected) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${matchResult.homeTeam.shortName} vs ${matchResult.awayTeam.shortName}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
            )
            Text(
                text = "Resultado: ${matchResult.fullTimeScoreHome} - ${matchResult.fullTimeScoreAway}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
            )
        }
    }
}




