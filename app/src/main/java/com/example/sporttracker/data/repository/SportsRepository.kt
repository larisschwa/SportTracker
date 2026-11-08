package com.example.sporttracker.data.repository

import android.content.Context
import android.util.Log
import com.example.sporttracker.data.local.CacheManager
import com.example.sporttracker.data.local.DefaultEvents
import com.example.sporttracker.data.local.DefaultTeams
import com.example.sporttracker.data.local.dao.GameDao
import com.example.sporttracker.data.local.dao.TeamDao
import com.example.sporttracker.data.local.entity.GameEntity
import com.example.sporttracker.data.local.entity.TeamEntity
import com.example.sporttracker.data.remote.NhlApiService
import com.example.sporttracker.data.remote.TheSportsDbEventDto
import com.example.sporttracker.data.remote.TheSportsDbService
import androidx.glance.appwidget.updateAll
import com.example.sporttracker.util.SearchUtils
import com.example.sporttracker.util.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

open class SportsRepository(
    private val context: Context,
    private val teamDao: TeamDao,
    private val gameDao: GameDao,
    private val sportsDbService: TheSportsDbService = TheSportsDbService.instance,
    private val nhlApiService: NhlApiService = NhlApiService.instance
) {
    private val cacheManager = CacheManager(context)

    suspend fun initializeTeams() = withContext(Dispatchers.IO) {
        // 1. Migrate any existing Hockey records to "Men's Hockey"
        teamDao.migrateHockeySport()
        gameDao.migrateHockeySport()

        // 2. Ensure all default teams exist and purge any duplicate team records
        for (defaultTeam in DefaultTeams.list) {
            teamDao.deleteDuplicateTeamsByName(defaultTeam.name, defaultTeam.id)
            val existing = teamDao.getTeamById(defaultTeam.id)
            if (existing == null) {
                teamDao.insert(defaultTeam)
            } else if (existing.logoUrl.isEmpty() && defaultTeam.logoUrl.isNotEmpty()) {
                teamDao.updateMissingLogo(defaultTeam.name, defaultTeam.logoUrl)
            }
        }

        // 3. Clear out outdated seed events and insert the verified schedules
        gameDao.clearSeedEvents()
        val seedEvents = DefaultEvents.generateInitialEvents()
        gameDao.insertAll(seedEvents)

        // 4. Purge any stray remote games that do not belong to user's followed teams
        val favs = teamDao.getFavoriteTeamsList()
        val favNames = favs.map { it.name }
        gameDao.purgeUnfollowedRemoteGames(favNames)
    }

    fun searchGames(query: String): Flow<List<GameEntity>> {
        val terms = SearchUtils.getAssociatedTerms(query)
        if (terms.isEmpty()) return flowOf(emptyList())
        if (terms.size == 1) return gameDao.searchGames(terms.first())

        val flows = terms.map { gameDao.searchGames(it) }
        return combine(flows) { resultsArray ->
            val map = linkedMapOf<String, GameEntity>()
            for (list in resultsArray) {
                for (game in list) {
                    if (!map.containsKey(game.id)) {
                        map[game.id] = game
                    }
                }
            }
            map.values.sortedWith(
                compareBy<GameEntity> { it.date }
                    .thenBy { it.timeCentral }
            )
        }
    }

    fun searchTeams(query: String): Flow<List<TeamEntity>> {
        val terms = SearchUtils.getAssociatedTerms(query)
        if (terms.isEmpty()) return flowOf(emptyList())

        val flows = terms.map { teamDao.searchTeams(it) }
        return combine(flows) { resultsArray ->
            val nameMap = linkedMapOf<String, TeamEntity>()
            for (list in resultsArray) {
                for (team in list) {
                    val key = team.name.trim().lowercase()
                    val existing = nameMap[key]
                    if (existing == null) {
                        nameMap[key] = team
                    } else {
                        // Prefer the entry with a valid logoUrl and preserve favorite state
                        val betterLogo = existing.logoUrl.ifEmpty { team.logoUrl }
                        val isFav = existing.isFavorite || team.isFavorite
                        nameMap[key] = existing.copy(logoUrl = betterLogo, isFavorite = isFav)
                    }
                }
            }
            nameMap.values.sortedWith(
                compareByDescending<TeamEntity> { it.isFavorite }
                    .thenBy { it.name }
            )
        }
    }

    suspend fun saveEvent(game: GameEntity) = withContext(Dispatchers.IO) {
        gameDao.insertAll(listOf(game))
    }

    suspend fun searchRemoteTeams(query: String): List<TeamEntity> = withContext(Dispatchers.IO) {
        val terms = SearchUtils.getAssociatedTerms(query).take(4)
        if (terms.isEmpty()) return@withContext emptyList()

        val combinedTeams = linkedMapOf<String, TeamEntity>()
        for (term in terms) {
            val trimmed = term.trim()
            if (trimmed.length < 2) continue
            try {
                val response = sportsDbService.searchTeamsByName(trimmed)
                val dtos = response.teams ?: emptyList()
                for (dto in dtos) {
                    val rawName = dto.strTeam.trim()
                    if (rawName.isEmpty()) continue
                    val nameKey = rawName.lowercase()
                    if (!combinedTeams.containsKey(nameKey)) {
                        val rawSport = dto.strSport.orEmpty()
                        val sport = if (rawSport.contains("Hockey", ignoreCase = true)) "Men's Hockey" else rawSport
                        val isRace = sport.contains("Motorsport", ignoreCase = true) ||
                            (dto.strLeague?.contains("Supercross", ignoreCase = true) == true) ||
                            (dto.strLeague?.contains("Motocross", ignoreCase = true) == true)
                        val existing = teamDao.getTeamById(dto.idTeam) ?: teamDao.getTeamByName(rawName)
                        combinedTeams[nameKey] = TeamEntity(
                            id = dto.idTeam,
                            name = rawName,
                            abbreviation = dto.strTeamShort ?: rawName.take(4).uppercase(),
                            logoUrl = dto.strBadge.orEmpty(),
                            sport = sport.ifEmpty { "Other" },
                            league = dto.strLeague.orEmpty(),
                            category = if (isRace) "Racing" else "Team",
                            isFavorite = existing?.isFavorite ?: false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w("SportsRepository", "Remote team search failed for '$term': ${e.message}")
            }
        }
        combinedTeams.values.toList()
    }

    suspend fun searchRemoteEvents(query: String): List<GameEntity> = withContext(Dispatchers.IO) {
        val terms = SearchUtils.getAssociatedTerms(query).take(3)
        if (terms.isEmpty()) return@withContext emptyList()

        val combinedEvents = linkedMapOf<String, GameEntity>()
        for (term in terms) {
            val trimmed = term.trim()
            if (trimmed.length < 2) continue
            try {
                val response = sportsDbService.searchEventsByName(trimmed)
                val dtos = response.events ?: response.results ?: emptyList()
                for (dto in dtos) {
                    val id = "sportsdb_${dto.idEvent}"
                    if (!combinedEvents.containsKey(id)) {
                        val rawSport = dto.strSport.orEmpty()
                        val sport = if (rawSport.contains("Hockey", ignoreCase = true)) "Men's Hockey" else rawSport
                        val isRace = sport.contains("Motorsport", ignoreCase = true) ||
                            (dto.strLeague?.contains("Supercross", ignoreCase = true) == true) ||
                            (dto.strLeague?.contains("Motocross", ignoreCase = true) == true)

                        val centralDate = TimeUtils.getCentralLocalDate(dto.strTimestamp, dto.dateEvent.orEmpty())
                        val centralTime = TimeUtils.formatToCentralTime(dto.strTimestamp, dto.strTime)

                        val title = dto.strEvent ?: if (isRace) {
                            "${dto.strLeague.orEmpty()} - ${dto.strVenue ?: "Event"}"
                        } else {
                            "${dto.strAwayTeam ?: "Away"} at ${dto.strHomeTeam ?: "Home"}"
                        }

                        combinedEvents[id] = GameEntity(
                            id = id,
                            title = title,
                            sport = sport.ifEmpty { "Other" },
                            league = dto.strLeague.orEmpty(),
                            date = TimeUtils.toCentralDateString(centralDate),
                            timeCentral = centralTime,
                            startTimeUtc = dto.strTimestamp.orEmpty(),
                            homeTeamId = dto.idHomeTeam.orEmpty(),
                            homeTeamAbbrev = dto.strHomeTeam?.take(4)?.uppercase().orEmpty(),
                            homeTeamName = dto.strHomeTeam ?: "Home",
                            homeTeamLogo = dto.strHomeTeamBadge.orEmpty(),
                            homeScore = dto.intHomeScore?.toIntOrNull(),
                            awayTeamId = dto.idAwayTeam.orEmpty(),
                            awayTeamAbbrev = dto.strAwayTeam?.take(4)?.uppercase().orEmpty(),
                            awayTeamName = dto.strAwayTeam ?: "Away",
                            awayTeamLogo = dto.strAwayTeamBadge.orEmpty(),
                            awayScore = dto.intAwayScore?.toIntOrNull(),
                            gameState = if ((dto.intHomeScore != null) && (dto.intAwayScore != null)) "FINAL" else "FUT",
                            venue = dto.strVenue.orEmpty(),
                            isRace = isRace,
                            entityId = dto.idHomeTeam ?: dto.idEvent
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w("SportsRepository", "Remote event search failed for '$term': ${e.message}")
            }
        }
        combinedEvents.values.toList()
    }

    fun getAllTeams(): Flow<List<TeamEntity>> = teamDao.getAllTeams()

    fun getFavoriteTeams(): Flow<List<TeamEntity>> = teamDao.getFavoriteTeams()

    suspend fun toggleFavorite(team: TeamEntity, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        val existing = teamDao.getTeamById(team.id)
        if (existing == null) {
            teamDao.insert(team.copy(isFavorite = isFavorite))
        } else {
            teamDao.updateFavorite(team.id, isFavorite)
        }
        if (isFavorite) {
            fetchScheduleForTeam(team, force = true)
        }
        try {
            com.example.sporttracker.widget.NextGameWidget().updateAll(context)
        } catch (_: Exception) {}
    }

    fun getGamesForDate(
        favoriteTeams: List<TeamEntity>,
        date: String
    ): Flow<List<GameEntity>> {
        if (favoriteTeams.isEmpty()) {
            return flowOf(emptyList())
        }
        val entityIds = favoriteTeams.map { it.id }
        val teamNames = favoriteTeams.map { it.name }
        return gameDao.getGamesForFavoritesOnDate(entityIds, teamNames, date)
    }

    fun getAllGamesForDate(date: String): Flow<List<GameEntity>> {
        return gameDao.getGamesForDate(date)
    }

    fun getGamesBetween(
        favoriteTeams: List<TeamEntity>,
        startDate: String,
        endDate: String
    ): Flow<List<GameEntity>> {
        if (favoriteTeams.isEmpty()) {
            return flowOf(emptyList())
        }
        val entityIds = favoriteTeams.map { it.id }
        val teamNames = favoriteTeams.map { it.name }
        return gameDao.getGamesForFavoritesBetween(entityIds, teamNames, startDate, endDate)
    }

    fun getAllGamesBetween(
        startDate: String,
        endDate: String
    ): Flow<List<GameEntity>> {
        return gameDao.getAllGamesBetween(startDate, endDate)
    }

    suspend fun refreshSchedules(force: Boolean = false): String = withContext(Dispatchers.IO) {
        if (cacheManager.isDailyBudgetExceeded()) {
            return@withContext "Daily API request limit reached. Displaying cached schedule."
        }

        val favorites = teamDao.getFavoriteTeamsList()
        if (favorites.isEmpty()) {
            return@withContext "Select teams or races to follow."
        }

        var fetchedCount = 0
        for (team in favorites) {
            if (cacheManager.shouldFetchForTeam(team.id, force)) {
                val success = fetchScheduleForTeam(team, force)
                if (success) fetchedCount++
            }
        }

        if (fetchedCount > 0) {
            try {
                com.example.sporttracker.widget.NextGameWidget().updateAll(context)
            } catch (_: Exception) {}
            "Updated schedule for $fetchedCount events/teams."
        } else {
            "Schedule is already up to date."
        }
    }

    private suspend fun fetchScheduleForTeam(team: TeamEntity, force: Boolean): Boolean = withContext(Dispatchers.IO) {
        if (!cacheManager.shouldFetchForTeam(team.id, force)) return@withContext false

        val gamesToInsert = mutableListOf<GameEntity>()

        try {
            // 1. If it's an NHL team, fetch from NHL API (keyless, completely free)
            if (team.league == "NHL" && team.abbreviation.isNotEmpty()) {
                try {
                    val nhlResp = nhlApiService.getTeamSeasonSchedule(team.abbreviation)
                    for (dto in nhlResp.games) {
                        val centralDate = TimeUtils.getCentralLocalDate(dto.startTimeUtc, dto.gameDate)
                        val centralTime = TimeUtils.formatToCentralTime(dto.startTimeUtc)
                        gamesToInsert.add(
                            GameEntity(
                                id = "nhl_${dto.id}",
                                title = "${dto.awayTeam.fullName} at ${dto.homeTeam.fullName}",
                                sport = "Men's Hockey",
                                league = "NHL",
                                date = TimeUtils.toCentralDateString(centralDate),
                                timeCentral = centralTime,
                                startTimeUtc = dto.startTimeUtc.orEmpty(),
                                homeTeamAbbrev = dto.homeTeam.abbrev,
                                homeTeamName = dto.homeTeam.fullName,
                                homeTeamLogo = dto.homeTeam.logo
                                    ?: "https://assets.nhle.com/mws/logos/teams/svg/${dto.homeTeam.abbrev}_light.svg",
                                homeScore = dto.homeTeam.score,
                                awayTeamAbbrev = dto.awayTeam.abbrev,
                                awayTeamName = dto.awayTeam.fullName,
                                awayTeamLogo = dto.awayTeam.logo
                                    ?: "https://assets.nhle.com/mws/logos/teams/svg/${dto.awayTeam.abbrev}_light.svg",
                                awayScore = dto.awayTeam.score,
                                gameState = dto.gameState,
                                venue = dto.venue?.default.orEmpty(),
                                entityId = team.id
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.w("SportsRepository", "NHL API fetch failed for ${team.name}: ${e.message}")
                }
            }

            // 2. TheSportsDB API for all sports, colleges, and races (only for numeric team IDs)
            val isNumericTeamId = team.id.toLongOrNull() != null
            if (gamesToInsert.isEmpty() && isNumericTeamId) {
                val nextEvents = sportsDbService.getNextEventsForTeam(team.id)
                nextEvents.events?.forEach { dto ->
                    if (dto.idHomeTeam == team.id || dto.idAwayTeam == team.id ||
                        dto.strHomeTeam?.contains(team.name, ignoreCase = true) == true ||
                        dto.strAwayTeam?.contains(team.name, ignoreCase = true) == true) {
                        gamesToInsert.add(sportsDbDtoToEntity(dto, team))
                    }
                }

                val lastEvents = sportsDbService.getLastEventsForTeam(team.id)
                lastEvents.results?.forEach { dto ->
                    if (dto.idHomeTeam == team.id || dto.idAwayTeam == team.id ||
                        dto.strHomeTeam?.contains(team.name, ignoreCase = true) == true ||
                        dto.strAwayTeam?.contains(team.name, ignoreCase = true) == true) {
                        gamesToInsert.add(sportsDbDtoToEntity(dto, team))
                    }
                }
            }

            if (gamesToInsert.isNotEmpty()) {
                gameDao.insertAll(gamesToInsert)
            }

            cacheManager.recordFetch(team.id)
            return@withContext true
        } catch (e: Exception) {
            Log.w("SportsRepository", "Failed to fetch for ${team.name}: ${e.message}")
            return@withContext false
        }
    }

    private fun sportsDbDtoToEntity(dto: TheSportsDbEventDto, currentTeam: TeamEntity): GameEntity {
        val homeScore = dto.intHomeScore?.toIntOrNull()
        val awayScore = dto.intAwayScore?.toIntOrNull()
        val state = when {
            homeScore != null && awayScore != null -> "FINAL"
            dto.strStatus?.contains("Live", ignoreCase = true) == true -> "LIVE"
            else -> "FUT"
        }

        val centralDate = TimeUtils.getCentralLocalDate(dto.strTimestamp, dto.dateEvent.orEmpty())
        val centralTime = TimeUtils.formatToCentralTime(dto.strTimestamp, dto.strTime)

        val isRace = currentTeam.category == "Racing" ||
            currentTeam.sport.contains("Motorsport", ignoreCase = true) ||
            dto.strSport?.contains("Motorsport", ignoreCase = true) == true ||
            (dto.strLeague?.contains("Supercross", ignoreCase = true) == true) ||
            (dto.strLeague?.contains("Motocross", ignoreCase = true) == true)

        val title = dto.strEvent ?: if (isRace) {
            "${dto.strLeague ?: currentTeam.league} - ${dto.strVenue ?: "Event"}"
        } else {
            "${dto.strAwayTeam ?: "Away"} at ${dto.strHomeTeam ?: "Home"}"
        }

        val rawSport = dto.strSport ?: currentTeam.sport
        val finalSport = if (rawSport.contains("Hockey", ignoreCase = true)) "Men's Hockey" else rawSport

        return GameEntity(
            id = "sportsdb_${dto.idEvent}",
            title = title,
            sport = finalSport,
            league = dto.strLeague ?: currentTeam.league,
            date = TimeUtils.toCentralDateString(centralDate),
            timeCentral = centralTime,
            startTimeUtc = dto.strTimestamp.orEmpty(),
            homeTeamId = dto.idHomeTeam.orEmpty(),
            homeTeamAbbrev = dto.strHomeTeam?.take(3)?.uppercase().orEmpty(),
            homeTeamName = dto.strHomeTeam ?: "Home",
            homeTeamLogo = dto.strHomeTeamBadge.orEmpty(),
            homeScore = homeScore,
            awayTeamId = dto.idAwayTeam.orEmpty(),
            awayTeamAbbrev = dto.strAwayTeam?.take(3)?.uppercase().orEmpty(),
            awayTeamName = dto.strAwayTeam ?: "Away",
            awayTeamLogo = dto.strAwayTeamBadge.orEmpty(),
            awayScore = awayScore,
            gameState = state,
            venue = dto.strVenue.orEmpty(),
            isRace = isRace,
            entityId = currentTeam.id
        )
    }
}
