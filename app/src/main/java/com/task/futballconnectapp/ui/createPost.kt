package com.task.futballconnectapp.ui

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable


data class TeamInfo(
    val id: Int,
    val shortName: String,
    val crest: String
)

data class MatchResult(
    val homeTeam: TeamInfo,
    val awayTeam: TeamInfo,
    val fullTimeScoreHome: Int,
    val fullTimeScoreAway: Int
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val matchResults = remember {
        listOf(
            MatchResult(TeamInfo(1, "Team A", "urlA"), TeamInfo(2, "Team B", "urlB"), 2, 1),
            MatchResult(TeamInfo(3, "Team C", "urlC"), TeamInfo(4, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(5, "Team C", "urlC"), TeamInfo(6, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(7, "Team C", "urlC"), TeamInfo(8, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(9, "Team C", "urlC"), TeamInfo(10, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(11, "Team C", "urlC"), TeamInfo(12, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(13, "Team C", "urlC"), TeamInfo(14, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(15, "Team C", "urlC"), TeamInfo(16, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(17, "Team C", "urlC"), TeamInfo(18, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(19, "Team C", "urlC"), TeamInfo(20, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(21, "Team C", "urlC"), TeamInfo(22, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(23, "Team C", "urlC"), TeamInfo(24, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(25, "Team C", "urlC"), TeamInfo(26, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(27, "Team C", "urlC"), TeamInfo(28, "Team D", "urlD"), 0, 3),
            MatchResult(TeamInfo(29, "Team C", "urlC"), TeamInfo(30, "Team D", "urlD"), 0, 3)
        )
    }
    val context = LocalContext.current
    fun handlePostCreation() {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            Toast.makeText(context, "Publicación creada: $title", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
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
            MatchResultList(matchResults = matchResults)
        }
        FloatingActionButton(
            onClick = { handlePostCreation() },
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
fun MatchResultList(matchResults: List<MatchResult>) {
    var selectedMatch by remember { mutableStateOf<MatchResult?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        matchResults.forEach { matchResult ->
            MatchResultCard(
                matchResult = matchResult,
                isSelected = selectedMatch == matchResult,
                onClick = { selectedMatch = matchResult }
            )
        }
    }
}

@Composable
fun MatchResultCard(
    matchResult: MatchResult,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
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
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor)            )
            Text(
                text = "Resultado: ${matchResult.fullTimeScoreHome} - ${matchResult.fullTimeScoreAway}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor)            )
        }
    }
}



@Composable
fun showToast(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun inputFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF4CAF50),
        unfocusedBorderColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
        focusedLabelColor = Color(0xFF4CAF50)
    )
}

@Preview(showBackground = true)
@Composable
fun CreatePostScreenPreview() {
    CreatePostScreen()
}
