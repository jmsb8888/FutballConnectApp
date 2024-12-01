package com.task.futballconnectapp.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    dataViewModel: DataViewModel,
    sharedPreferencesViewModel: SharedPreferencesViewModel
) {
    var idUser = sharedPreferencesViewModel.getIdUser()
    if (idUser != 0) {
        LaunchedEffect(idUser) {
            navController.navigate("home")
        }
        return
    }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isPasswordInvalid by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userState by dataViewModel.user.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.font),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center),
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { /*  */ }
                    ),
                    colors = inputFieldColors()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /* */ }
                    ),
                    colors = inputFieldColors()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (username.isNotEmpty() && password.isNotEmpty()) {
                            isError = false
                            isPasswordInvalid = false
                            isLoading = true
                            dataViewModel.fetchUserByEmail(username)
                        } else {
                            isError = true
                            Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Iniciar Sesión")
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (isPasswordInvalid) {
                    Text(
                        text = "Usuario o contraseña incorrectos",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "¿No tienes cuenta? Crear una nueva",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            navController.navigate("register")
                        }
                )
            }
        }
    }
    LaunchedEffect(userState.user, isLoading) {
        if (isLoading && userState.user != null) {
            userState.user?.let { user ->
                if (user.password == password) {
                    Log.d("LoginScreen", "Usuario logueado: ${user.name}")
                    Log.d("LoginScreen", "SharedPreferences: setIdUser(${user.id})")
                    sharedPreferencesViewModel.setIdUser(user.id)
                    sharedPreferencesViewModel.setUserName(user.name)
                    sharedPreferencesViewModel.setUserImage(user.imagePerfil ?: "")
                    navController.navigate("home")
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT)
                        .show()
                    isPasswordInvalid = true
                }
            }
            isLoading = false
        }
    }
}
