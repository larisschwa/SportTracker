package com.example.sporttracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey
    val id: String, // TheSportsDB team ID, league ID, or unique key
    val name: String, // e.g. "Minnesota Wild", "Minnesota Golden Gophers", "Monster Energy Supercross"
    val abbreviation: String,
    val logoUrl: String,
    val sport: String = "Ice Hockey", // Ice Hockey, College Hockey, Motorsport, Basketball, etc.
    val league: String = "NHL", // NHL, NCAA, Supercross, Formula 1, etc.
    val division: String = "",
    val category: String = "Team", // "Team" or "Racing"
    val isFavorite: Boolean = false
)
