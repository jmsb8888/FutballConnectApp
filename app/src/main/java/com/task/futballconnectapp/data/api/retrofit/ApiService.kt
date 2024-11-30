package com.task.futballconnectapp.data.api.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("competitions")
    suspend fun getCompetitionsRaw(): Response<ResponseBody>

    @GET("competitions/{competitionId}/matches?season=2024&status=FINISHED")
    suspend fun getMatches(
        @Path("competitionId") competitionId: Int,
    ): Response<ResponseBody>

    @GET("competitions/{competitionId}/teams?season=2024")
    suspend fun getTeams(
        @Path("competitionId") competitionId: Int,
    ): Response<ResponseBody>

}