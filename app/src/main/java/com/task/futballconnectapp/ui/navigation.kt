package com.task.futballconnectapp.ui

import AppHeader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.futballconnectapp.data.viewmodel.ApiViewModel
import com.task.futballconnectapp.data.viewmodel.DataViewModel
import com.task.futballconnectapp.data.viewmodel.RoomViewModel
import com.task.futballconnectapp.data.viewmodel.SharedPreferencesViewModel

@Composable
fun MyApp(
    sharedPreferencesViewModel: SharedPreferencesViewModel,
    dataViewModel: DataViewModel,
    roomViewModel: RoomViewModel
) {
    val navController = rememberNavController()
    val apiViewModel: ApiViewModel = viewModel()
    val postMatchesState = dataViewModel.postMatch.collectAsState().value
    val postPlayersState = dataViewModel.postPlayers.collectAsState().value
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (isInternetAvailable(context)) {
            dataViewModel.fetchAllPostMatches(sharedPreferencesViewModel.getIdUser())
            dataViewModel.fetchAllPostPlayer(sharedPreferencesViewModel.getIdUser())
        } else {
            roomViewModel.getAllPostsPlayer(sharedPreferencesViewModel.getIdUser()) {
                postPlayersState.postPlayers = it
            }
            roomViewModel.getAllPostsMatches(sharedPreferencesViewModel.getIdUser()) {
                postMatchesState.postMatches = it
            }
        }

    }
    Scaffold(
        topBar = {
            AppHeader(
                onLogoutClick = { /*  */ },
                navController,
                dataViewModel,
                sharedPreferencesViewModel
            )
        },
        bottomBar = { BottomMenu(navController) },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("home") {
                        FootballPostsScreen(
                            navController = navController,
                            "createPost",
                            postMatchesState.postMatches,
                            dataViewModel,
                            sharedPreferencesViewModel,
                            roomViewModel
                        )
                    }
                    composable("results") {
                        FootballPostsScreen(
                            navController = navController,
                            "createPostPlayer",
                            postPlayersState.postPlayers,
                            dataViewModel,
                            sharedPreferencesViewModel,
                            roomViewModel
                        )
                    }
                    composable("createPost") {
                        CreatePostScreen(
                            navController = navController,
                            apiViewModel,
                            dataViewModel,
                            sharedPreferencesViewModel,
                            roomViewModel
                        )
                    }
                    composable("createPostPlayer") {
                        CompetitionScreen(
                            onTeamSelected = {
                            },
                            navController = navController,
                            apiViewModel,
                            dataViewModel,
                            sharedPreferencesViewModel,
                            roomViewModel
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            dataViewModel,
                            sharedPreferencesViewModel,
                            roomViewModel
                        )
                    }
                    composable("register") {
                        UserRegistrationScreen(
                            navController = navController,
                            apiViewModel,
                            dataViewModel,
                            sharedPreferencesViewModel,
                            roomViewModel
                        )
                    }
                }
            }
        }
    )
}








