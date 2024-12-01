package com.task.futballconnectapp.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.task.futballconnectapp.data.api.models.CompetitionD
import com.task.futballconnectapp.data.api.models.ResultsD
import com.task.futballconnectapp.data.api.models.Team
import com.task.futballconnectapp.data.bd.models.MatchResult
import com.task.futballconnectapp.data.bd.models.Post
import com.task.futballconnectapp.data.viewmodel.ApiViewModel
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    apiViewModel: ApiViewModel,
    dataViewModel: DataViewModel,
    sharedPreferencesViewModel: SharedPreferencesViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val competitionsState = apiViewModel.competitions.collectAsState().value
    val matchResults = apiViewModel.results.collectAsState().value
    val context = LocalContext.current
    var selectedMatch by remember { mutableStateOf<ResultsD?>(null) }
    var selectedCompetition by remember { mutableStateOf<CompetitionD?>(null) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun handlePostCreation() {
        if (title.isNotEmpty() && description.isNotEmpty() && selectedMatch != null) {
            val matchResult = selectedMatch?.let {
                MatchResult(
                    id = 1,
                    homeTeam = it.homeTeam.shortName,
                    awayTeam = it.awayTeam.shortName,
                    fullTimeScoreHome = it.fullTimeScoreHome,
                    fullTimeScoreAway = it.fullTimeScoreAway
                )
            }
            val result = dataViewModel.createPostMatch(matchResult!!)
            result?.let {
                val post = Post(
                    userName = sharedPreferencesViewModel.getUserName(),
                    userProfileImageUrl = sharedPreferencesViewModel.getUserImage(),
                    mainImageUrl = "image",
                    title = title,
                    description = description,
                    matchResult = it,
                    person = null,
                    isLiked = false
                )
                dataViewModel.createPost(post)
                Toast.makeText(context, "Publicación creada: $title", Toast.LENGTH_SHORT).show()
                navController.navigate("home")
            }
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
                CircularProgressIndicator()
            } else if (competitionsState.competitions != null) {
                CompetitionList(
                    competitions = competitionsState.competitions,
                    selectedCompetition = selectedCompetition,
                    onCompetitionClick = { competition ->
                        selectedCompetition = competition
                        apiViewModel.fetchAndExtractResults(competition.id)
                    }
                )
            }

            if (matchResults.isLoading && selectedMatch != null) {
                CircularProgressIndicator()
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
            onClick = {
                coroutineScope.launch {
                    handlePostCreation()
                }
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




