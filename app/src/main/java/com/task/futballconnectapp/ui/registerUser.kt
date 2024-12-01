package com.task.futballconnectapp.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.futballconnectapp.R
import com.task.futballconnectapp.data.api.models.CompetitionD
import com.task.futballconnectapp.data.api.models.Team
import com.task.futballconnectapp.data.bd.models.User
import com.task.futballconnectapp.data.viewmodel.ApiViewModel
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun UserRegistrationScreen(
    navController: NavController,
    apiViewModel: ApiViewModel,
    dataViewModel: DataViewModel,
    sharedPreferencesViewModel: SharedPreferencesViewModel
) {
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
    val imagePerfil = selectedTeam?.crest ?: "default_image_path"
    val isFormValid = firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() &&
            password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    val coroutineScope = rememberCoroutineScope()
    val handleRegistration: suspend () -> Unit = {
        if (isFormValid && selectedTeam != null) {
            val newUser = User(
                id = 0,
                name = "$firstName $lastName",
                email = email,
                password = password,
                imagePerfil = imagePerfil
            )
            dataViewModel.createUser(newUser).let { user ->
                if (user != null) {
                    sharedPreferencesViewModel.setIdUser(user.id)
                    sharedPreferencesViewModel.setUserName(user.name)
                    sharedPreferencesViewModel.setUserImage(user.imagePerfil ?: "")
                    Toast.makeText(context, "Usuario registrado: ${user.name}", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate("home")
                } else {
                    Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(
                context,
                "Las contraseñas no coinciden o falta información",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
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
                CircularProgressIndicator()
            } else if (competitionsState.competitions != null) {
                CompetitionList(
                    competitions = competitionsState.competitions,
                    selectedCompetition = selectedCompetition,
                    onCompetitionClick = { competition ->
                        selectedCompetition = competition
                        apiViewModel.fetchAndExtractTeams(competition.id)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (teamsState.isLoading) {
                CircularProgressIndicator()
            } else if (teamsState.teams != null && selectedCompetition != null) {
                teamsList(
                    teams = teamsState.teams,
                    selectedTeam = selectedTeam,
                    onTeamClick = { team ->
                        selectedTeam = if (selectedTeam == team) null else team
                    }
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center)
                .padding(top = if (selectedCompetition != null) 250.dp else 100.dp)
                .verticalScroll(rememberScrollState()),
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
                Button(
                    onClick = {
                        coroutineScope.launch {
                            handleRegistration()

                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    enabled = isFormValid
                ) {
                    Text("Registrar")
                }
            }
        }
    }
}
