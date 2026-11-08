package com.example.sporttracker.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TheSportsDbService {

    @GET("eventsnext.php")
    suspend fun getNextEventsForTeam(
        @Query("id") teamId: String
    ): TheSportsDbEventsResponse

    @GET("eventslast.php")
    suspend fun getLastEventsForTeam(
        @Query("id") teamId: String
    ): TheSportsDbEventsResponse

    @GET("search_all_teams.php")
    suspend fun getTeamsInLeague(
        @Query("l") league: String
    ): TheSportsDbTeamsResponse

    @GET("searchteams.php")
    suspend fun searchTeamsByName(
        @Query("t") teamName: String
    ): TheSportsDbTeamsResponse

    @GET("searchevents.php")
    suspend fun searchEventsByName(
        @Query("e") eventName: String
    ): TheSportsDbEventsResponse

    companion object {
        private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/123/"

        val instance: TheSportsDbService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheSportsDbService::class.java)
        }
    }
}
