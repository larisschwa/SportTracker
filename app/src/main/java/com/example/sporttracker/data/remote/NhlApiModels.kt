package com.example.sporttracker.data.remote

import com.google.gson.annotations.SerializedName

data class TeamScheduleResponse(
    @SerializedName("currentDate") val currentDate: String? = null,
    @SerializedName("games") val games: List<NhlGameDto> = emptyList()
)

data class LeagueScheduleResponse(
    @SerializedName("currentDate") val currentDate: String? = null,
    @SerializedName("gameWeek") val gameWeek: List<GameWeekDto> = emptyList()
)

data class GameWeekDto(
    @SerializedName("date") val date: String,
    @SerializedName("numberOfGames") val numberOfGames: Int = 0,
    @SerializedName("games") val games: List<NhlGameDto> = emptyList()
)

data class NhlGameDto(
    @SerializedName("id") val id: Long,
    @SerializedName("gameDate") val gameDate: String,
    @SerializedName("startTimeUTC") val startTimeUtc: String? = null,
    @SerializedName("gameState") val gameState: String = "FUT",
    @SerializedName("venue") val venue: LocalizedText? = null,
    @SerializedName("homeTeam") val homeTeam: NhlTeamDto,
    @SerializedName("awayTeam") val awayTeam: NhlTeamDto
)

data class NhlTeamDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("abbrev") val abbrev: String,
    @SerializedName("placeName") val placeName: LocalizedText? = null,
    @SerializedName("commonName") val commonName: LocalizedText? = null,
    @SerializedName("logo") val logo: String? = null,
    @SerializedName("score") val score: Int? = null
) {
    val fullName: String
        get() {
            val place = placeName?.default.orEmpty()
            val common = commonName?.default.orEmpty()
            return if (place.isNotEmpty() && common.isNotEmpty()) "$place $common"
            else if (common.isNotEmpty()) common
            else abbrev
        }
}

data class LocalizedText(
    @SerializedName("default") val default: String = ""
)
