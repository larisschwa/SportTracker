package com.example.sporttracker.data.remote

import com.google.gson.annotations.SerializedName

data class TheSportsDbEventsResponse(
    @SerializedName("events")
    val events: List<TheSportsDbEventDto>? = null,
    @SerializedName("results")
    val results: List<TheSportsDbEventDto>? = null
)

data class TheSportsDbEventDto(
    @SerializedName("idEvent") val idEvent: String,
    @SerializedName("strEvent") val strEvent: String? = null,
    @SerializedName("strSport") val strSport: String? = null,
    @SerializedName("idLeague") val idLeague: String? = null,
    @SerializedName("strLeague") val strLeague: String? = null,
    @SerializedName("strHomeTeam") val strHomeTeam: String? = null,
    @SerializedName("strAwayTeam") val strAwayTeam: String? = null,
    @SerializedName("idHomeTeam") val idHomeTeam: String? = null,
    @SerializedName("idAwayTeam") val idAwayTeam: String? = null,
    @SerializedName("intHomeScore") val intHomeScore: String? = null,
    @SerializedName("intAwayScore") val intAwayScore: String? = null,
    @SerializedName("dateEvent") val dateEvent: String? = null,
    @SerializedName("strTime") val strTime: String? = null,
    @SerializedName("strTimestamp") val strTimestamp: String? = null,
    @SerializedName("strVenue") val strVenue: String? = null,
    @SerializedName("strStatus") val strStatus: String? = null,
    @SerializedName("strHomeTeamBadge") val strHomeTeamBadge: String? = null,
    @SerializedName("strAwayTeamBadge") val strAwayTeamBadge: String? = null,
    @SerializedName("strThumb") val strThumb: String? = null
)

data class TheSportsDbTeamsResponse(
    @SerializedName("teams")
    val teams: List<TheSportsDbTeamDto>? = null
)

data class TheSportsDbTeamDto(
    @SerializedName("idTeam") val idTeam: String,
    @SerializedName("strTeam") val strTeam: String,
    @SerializedName("strTeamShort") val strTeamShort: String? = null,
    @SerializedName("strSport") val strSport: String? = null,
    @SerializedName("strLeague") val strLeague: String? = null,
    @SerializedName("strBadge") val strBadge: String? = null,
    @SerializedName("strDescriptionEN") val strDescription: String? = null
)
