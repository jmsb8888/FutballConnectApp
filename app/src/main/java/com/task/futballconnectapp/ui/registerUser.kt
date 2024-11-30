package com.task.futballconnectapp.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.task.futballconnectapp.R
import com.task.futballconnectapp.data.api.models.CompetitionD
import com.task.futballconnectapp.data.api.models.Team
import com.task.futballconnectapp.data.viewmodel.ApiViewModel


@Composable
fun UserRegistrationScreen(navController: NavController, apiViewModel: ApiViewModel) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val competitionsState = apiViewModel.competitions.collectAsState().value
    val teamsState = apiViewModel.teams.collectAsState().value
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var selectedCompetition by remember { mutableStateOf<CompetitionD?>(null) }
    val context = LocalContext.current

    // Función para manejar el registro
    fun handleRegistration() {
        if (password == confirmPassword && selectedTeam != null) {
            Toast.makeText(context, "Usuario registrado: $firstName $lastName", Toast.LENGTH_SHORT)
                .show()
            navController.navigate("home")
        } else {
            Toast.makeText(context, "Las contraseñas no coinciden o no se ha seleccionado un equipo", Toast.LENGTH_SHORT).show()
        }
    }

    // Composición para la pantalla de registro de usuario
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.font),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.TopCenter)
        ) {
            if (competitionsState.isLoading) {
                CircularProgressIndicator() // Indicador de carga de competiciones
            } else if (competitionsState.competitions != null) {
                // Listado de competiciones
                CompetitionList(
                    competitions = competitionsState.competitions,
                    selectedCompetition = selectedCompetition,
                    onCompetitionClick = { competition ->
                        selectedCompetition = competition
                        apiViewModel.fetchAndExtractTeams(competition.id)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaciado entre competiciones y equipos

            if (teamsState.isLoading) {
                CircularProgressIndicator() // Indicador de carga de equipos
            } else if (teamsState.teams != null && selectedCompetition != null) {
                // Mostrar lista de equipos solo si hay una competencia seleccionada
                teamsList(
                    teams = teamsState.teams,
                    selectedTeam = selectedTeam,
                    onTeamClick = { team ->
                        selectedTeam = if (selectedTeam == team) null else team
                    }
                )
            }
        }
        // Card para el formulario de registro
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center)
                .padding(top = if (selectedCompetition != null) 250.dp else 100.dp)
                .verticalScroll(rememberScrollState()), // Desplazar hacia abajo si se ha seleccionado una competencia
            elevation = CardDefaults.cardElevation(4.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF000000).copy(alpha = 0.7f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registro de Usuario",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Campos del formulario
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = inputFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = inputFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    colors = inputFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    colors = inputFieldColors()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    colors = inputFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de registro habilitado solo si se ha seleccionado un equipo
                Button(
                    onClick = { handleRegistration() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    enabled = selectedTeam != null // Solo habilitar si se ha seleccionado un equipo
                ) {
                    Text("Registrar")
                }
            }
        }


    }
}
