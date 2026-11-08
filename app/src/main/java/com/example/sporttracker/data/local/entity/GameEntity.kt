package com.example.sporttracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: String,
    val title: String = "",
    val sport: String = "Ice Hockey",
    val league: String = "NHL",
    val date: String, // Central Time ISO date, e.g. "2026-11-10"
    val timeCentral: String = "", // e.g. "7:00 PM CT"
    val startTimeUtc: String = "",
    val homeTeamId: String = "",
    val homeTeamAbbrev: String = "",
    val homeTeamName: String = "",
    val homeTeamLogo: String = "",
    val homeScore: Int? = null,
    val awayTeamId: String = "",
    val awayTeamAbbrev: String = "",
    val awayTeamName: String = "",
    val awayTeamLogo: String = "",
    val awayScore: Int? = null,
    val gameState: String = "FUT", // FUT, PRE, LIVE, FINAL
    val venue: String = "",
    val isRace: Boolean = false, // true for Supercross, F1, NASCAR races
    val entityId: String = "" // tracks the followed team or racing series ID
)
