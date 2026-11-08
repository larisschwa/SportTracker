package com.example.sporttracker.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface NhlApiService {

    @GET("v1/club-schedule-season/{teamAbbrev}/now")
    suspend fun getTeamSeasonSchedule(
        @Path("teamAbbrev") teamAbbrev: String
    ): TeamScheduleResponse

    @GET("v1/schedule/{date}")
    suspend fun getLeagueSchedule(
        @Path("date") date: String
    ): LeagueScheduleResponse

    companion object {
        private const val BASE_URL = "https://api-web.nhle.com/"

        val instance: NhlApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NhlApiService::class.java)
        }
    }
}
