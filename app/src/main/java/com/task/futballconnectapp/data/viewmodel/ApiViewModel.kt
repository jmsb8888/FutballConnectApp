package com.task.futballconnectapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.futballconnectapp.UiState.Api.CompetitionsUiState
import com.task.futballconnectapp.UiState.Api.ResultsUiState
import com.task.futballconnectapp.UiState.Api.TeamsUiState
import com.task.futballconnectapp.data.api.models.Coach
import com.task.futballconnectapp.data.api.models.CompetitionD
import com.task.futballconnectapp.data.api.models.Player
import com.task.futballconnectapp.data.api.models.ResultsD
import com.task.futballconnectapp.data.api.models.Team
import com.task.futballconnectapp.data.api.models.TeamInfo
import com.task.futballconnectapp.data.api.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val retrofitApi: ApiService
): ViewModel() {
    private val _competitions = MutableStateFlow(CompetitionsUiState())
    val competitions = _competitions.asStateFlow()

    private val _results = MutableStateFlow(ResultsUiState())
    val results = _results.asStateFlow()

    private val _teams = MutableStateFlow(TeamsUiState())
    val teams = _teams.asStateFlow()

    init {
        fetchAndExtractCompetitions()
    }
    private fun fetchAndExtractCompetitions() {
        viewModelScope.launch {
            val response = getCompetitions()
            if (response != null) {
                val competitions = extractCompetitions(response)
                _competitions.value = _competitions.value.copy(
                    isLoading = false,
                    competitions = competitions
                )
            }
        }
    }

    private suspend fun getCompetitions(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofitApi.getCompetitionsRaw()
                if (response.isSuccessful) {
                    response.body()?.string()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }


    private fun extractCompetitions(jsonString: String): List<CompetitionD> {
        val json = Json { ignoreUnknownKeys = true }
        val root = json.decodeFromString<JsonObject>(jsonString)
        val competitionsArray = root["competitions"]?.jsonArray ?: return emptyList()
        return competitionsArray.mapNotNull { competitionJson ->
            try {
                val id = competitionJson.jsonObject["id"]?.jsonPrimitive?.int ?: return@mapNotNull null
                val name = competitionJson.jsonObject["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val emblem = competitionJson.jsonObject["emblem"]?.jsonPrimitive?.content ?: return@mapNotNull null
                CompetitionD(id = id, name = name, emblem = emblem)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun fetchAndExtractResults(idCompetition: Int) {
        viewModelScope.launch {
            val response = getResults(idCompetition)
            if (response != null) {
                val results = extractResults(response)
                _results.value = _results.value.copy(
                    isLoading = false,
                    results = results
                )
            }
        }
    }

    private suspend fun getResults(idCompetition: Int): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofitApi.getMatches(idCompetition)
                if (response.isSuccessful) {
                    response.body()?.string()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun extractResults(jsonString: String): List<ResultsD> {
        val json = Json { ignoreUnknownKeys = true }
        val root = json.decodeFromString<JsonObject>(jsonString)
        val matchesArray = root["matches"]?.jsonArray ?: return emptyList()
        return matchesArray.mapNotNull { matchJson ->
            try {
                val homeTeam = matchJson.jsonObject["homeTeam"]?.jsonObject
                val awayTeam = matchJson.jsonObject["awayTeam"]?.jsonObject
                val homeTeamInfo = TeamInfo(
                    id = homeTeam?.get("id")?.jsonPrimitive?.int ?: return@mapNotNull null,
                    shortName = homeTeam["shortName"]?.jsonPrimitive?.content ?: return@mapNotNull null,
                    crest = homeTeam["crest"]?.jsonPrimitive?.content ?: return@mapNotNull null
                )
                val awayTeamInfo = TeamInfo(
                    id = awayTeam?.get("id")?.jsonPrimitive?.int ?: return@mapNotNull null,
                    shortName = awayTeam["shortName"]?.jsonPrimitive?.content ?: return@mapNotNull null,
                    crest = awayTeam["crest"]?.jsonPrimitive?.content ?: return@mapNotNull null
                )
                val fullTimeHome = matchJson.jsonObject["score"]?.jsonObject
                    ?.get("fullTime")?.jsonObject?.get("home")?.jsonPrimitive?.int ?: return@mapNotNull null
                val fullTimeAway = matchJson.jsonObject["score"]?.jsonObject
                    ?.get("fullTime")?.jsonObject?.get("away")?.jsonPrimitive?.int ?: return@mapNotNull null
                ResultsD(
                    homeTeam = homeTeamInfo,
                    awayTeam = awayTeamInfo,
                    fullTimeScoreHome = fullTimeHome,
                    fullTimeScoreAway = fullTimeAway
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    fun fetchAndExtractTeams(idCompetition: Int) {
        viewModelScope.launch {
            val response = getTeams(idCompetition)
            if (response != null) {
                val teams = extractTeams(response)
                _teams.value = _teams.value.copy(
                    isLoading = false,
                    teams = teams
                )
            }
        }
    }

    private suspend fun getTeams(idCompetition: Int): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofitApi.getTeams(idCompetition)
                if (response.isSuccessful) {
                    response.body()?.string()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }


    private fun extractTeams(jsonString: String): List<Team> {
        val json = Json { ignoreUnknownKeys = true }
        val root = json.decodeFromString<JsonObject>(jsonString)
        val teamsArray = root["teams"]?.jsonArray ?: return emptyList()
        return teamsArray.mapNotNull { teamJson ->
            try {
                val id = teamJson.jsonObject["id"]?.jsonPrimitive?.int ?: return@mapNotNull null
                val name = teamJson.jsonObject["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val shortName = teamJson.jsonObject["shortName"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val crest = teamJson.jsonObject["crest"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val venue = teamJson.jsonObject["venue"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val clubColors = teamJson.jsonObject["clubColors"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val coachJson = teamJson.jsonObject["coach"]?.jsonObject
                val coach = coachJson?.let {
                    Coach(
                        id = it["id"]?.jsonPrimitive?.int ?: return@let null,
                        name = it["name"]?.jsonPrimitive?.content ?: return@let null,
                        dateOfBirth = it["dateOfBirth"]?.jsonPrimitive?.content ?: return@let null,
                        nationality = it["nationality"]?.jsonPrimitive?.content ?: return@let null
                    )
                } ?: return@mapNotNull null
                val squadJson = teamJson.jsonObject["squad"]?.jsonArray
                val squad = squadJson?.mapNotNull { playerJson ->
                    val id = playerJson.jsonObject["id"]?.jsonPrimitive?.int ?: return@mapNotNull null
                    val name = playerJson.jsonObject["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
                    val position = playerJson.jsonObject["position"]?.jsonPrimitive?.content ?: return@mapNotNull null
                    val dateOfBirth = playerJson.jsonObject["dateOfBirth"]?.jsonPrimitive?.content ?: return@mapNotNull null
                    val nationality = playerJson.jsonObject["nationality"]?.jsonPrimitive?.content ?: return@mapNotNull null
                    Player(id, name, position, dateOfBirth, nationality)
                } ?: emptyList()
                Team(id, name, shortName, crest, venue, clubColors, coach, squad)
            } catch (e: Exception) {
                null
            }
        }
    }
}
