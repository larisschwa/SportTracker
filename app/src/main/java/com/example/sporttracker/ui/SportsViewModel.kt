package com.example.sporttracker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporttracker.data.local.AppDatabase
import com.example.sporttracker.data.local.entity.GameEntity
import com.example.sporttracker.data.local.entity.TeamEntity
import com.example.sporttracker.data.repository.SportsRepository
import com.example.sporttracker.util.TimeUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
open class SportsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = SportsRepository(application, db.teamDao(), db.gameDao())

    val allTeams: StateFlow<List<TeamEntity>> = repository.getAllTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteTeams: StateFlow<List<TeamEntity>> = repository.getFavoriteTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableSports: StateFlow<List<String>> = allTeams.map { teams ->
        teams.map { it.sport }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSport = MutableStateFlow<String?>(null)
    val selectedSport: StateFlow<String?> = _selectedSport.asStateFlow()

    private val _selectedDate = MutableStateFlow(TimeUtils.currentCentralDate())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _weekStartDate = MutableStateFlow(
        TimeUtils.currentCentralDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    )
    val weekStartDate: StateFlow<LocalDate> = _weekStartDate.asStateFlow()

    private val _filterOnlyFavorites = MutableStateFlow(true)
    val filterOnlyFavorites: StateFlow<Boolean> = _filterOnlyFavorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _onlineTeams = MutableStateFlow<List<TeamEntity>>(emptyList())
    private val _onlineGames = MutableStateFlow<List<GameEntity>>(emptyList())

    private val _isSearchingOnline = MutableStateFlow(false)
    val isSearchingOnline: StateFlow<Boolean> = _isSearchingOnline.asStateFlow()

    val searchResultsTeams: StateFlow<List<TeamEntity>> = combine(
        _searchQuery.flatMapLatest { query ->
            if (query.isBlank()) kotlinx.coroutines.flow.flowOf(emptyList())
            else repository.searchTeams(query.trim())
        },
        _onlineTeams,
        favoriteTeams
    ) { localTeams, onlineTeams, favs ->
        val favIds = favs.map { it.id }.toSet()
        val favNames = favs.map { it.name.trim().lowercase() }.toSet()

        val combinedMap = linkedMapOf<String, TeamEntity>()
        for (team in localTeams) {
            val key = team.name.trim().lowercase()
            val isFav = favIds.contains(team.id) || favNames.contains(key)
            combinedMap[key] = team.copy(isFavorite = isFav)
        }
        for (team in onlineTeams) {
            val key = team.name.trim().lowercase()
            val existing = combinedMap[key]
            if (existing == null) {
                val isFav = favIds.contains(team.id) || favNames.contains(key)
                combinedMap[key] = team.copy(isFavorite = isFav)
            } else if (existing.logoUrl.isEmpty() && team.logoUrl.isNotEmpty()) {
                combinedMap[key] = existing.copy(logoUrl = team.logoUrl)
            }
        }
        combinedMap.values.sortedWith(
            compareByDescending<TeamEntity> { it.isFavorite }
                .thenBy { it.name }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchResultsGames: StateFlow<List<GameEntity>> = combine(
        _searchQuery.flatMapLatest { query ->
            if (query.isBlank()) kotlinx.coroutines.flow.flowOf(emptyList())
            else repository.searchGames(query.trim())
        },
        _onlineGames
    ) { localGames, onlineGames ->
        val combinedMap = linkedMapOf<String, GameEntity>()
        for (game in localGames) {
            combinedMap[game.id] = game
        }
        for (game in onlineGames) {
            val isDuplicate = combinedMap.values.any { existing ->
                existing.date == game.date && (
                    existing.title.equals(game.title, ignoreCase = true) ||
                    (existing.homeTeamName.equals(game.homeTeamName, ignoreCase = true) &&
                     existing.awayTeamName.equals(game.awayTeamName, ignoreCase = true))
                )
            }
            if (!combinedMap.containsKey(game.id) && !isDuplicate) {
                combinedMap[game.id] = game
            }
        }
        combinedMap.values.sortedWith(
            compareBy<GameEntity> { it.date }
                .thenBy { it.timeCentral }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gamesForSelectedDate: StateFlow<List<GameEntity>> = combine(
        _selectedDate,
        favoriteTeams,
        _filterOnlyFavorites,
        _selectedSport
    ) { date, favs, onlyFavs, sportFilter ->
        val dateStr = TimeUtils.toCentralDateString(date)
        Triple(onlyFavs, favs, dateStr) to sportFilter
    }.flatMapLatest { (meta, sportFilter) ->
        val (onlyFavs, favs, dateStr) = meta
        val flow = if (onlyFavs) {
            repository.getGamesForDate(favs, dateStr)
        } else {
            repository.getAllGamesForDate(dateStr)
        }
        flow.map { games ->
            if (sportFilter != null) {
                games.filter { it.sport.equals(sportFilter, ignoreCase = true) }
            } else {
                games
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weekGamesCount: StateFlow<Map<LocalDate, Int>> = combine(
        _weekStartDate,
        favoriteTeams,
        _filterOnlyFavorites,
        _selectedSport
    ) { start, favs, onlyFavs, sportFilter ->
        val end = start.plusDays(6)
        val startStr = TimeUtils.toCentralDateString(start)
        val endStr = TimeUtils.toCentralDateString(end)
        Triple(onlyFavs, favs, startStr to endStr) to (start to sportFilter)
    }.flatMapLatest { (queryData, meta) ->
        val (onlyFavs, favs, dates) = queryData
        val (startStr, endStr) = dates
        val (start, sportFilter) = meta
        val flow = if (onlyFavs) {
            repository.getGamesBetween(favs, startStr, endStr)
        } else {
            repository.getAllGamesBetween(startStr, endStr)
        }
        flow.map { games ->
            val filtered = if (sportFilter != null) {
                games.filter { it.sport.equals(sportFilter, ignoreCase = true) }
            } else {
                games
            }
            val map = mutableMapOf<LocalDate, Int>()
            for (i in 0..6) {
                val d = start.plusDays(i.toLong())
                val dStr = TimeUtils.toCentralDateString(d)
                map[d] = filtered.count { it.date == dStr }
            }
            map
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    init {
        viewModelScope.launch {
            repository.initializeTeams()
            refresh(force = false)
        }

        viewModelScope.launch {
            _searchQuery
                .debounce(350)
                .collectLatest { query ->
                    val trimmed = query.trim()
                    if (trimmed.length < 2) {
                        _onlineTeams.value = emptyList()
                        _onlineGames.value = emptyList()
                        _isSearchingOnline.value = false
                        return@collectLatest
                    }
                    _isSearchingOnline.value = true
                    try {
                        val remoteTeams = repository.searchRemoteTeams(trimmed)
                        val remoteEvents = repository.searchRemoteEvents(trimmed)
                        _onlineTeams.value = remoteTeams
                        _onlineGames.value = remoteEvents
                    } catch (e: Exception) {
                        Log.w("SportsViewModel", "Online search failed: ${e.message}")
                    } finally {
                        _isSearchingOnline.value = false
                    }
                }
        }
    }

    fun selectSport(sport: String?) {
        _selectedSport.value = sport
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        val currentStart = _weekStartDate.value
        val currentEnd = currentStart.plusDays(6)
        if (date.isBefore(currentStart) || date.isAfter(currentEnd)) {
            _weekStartDate.value = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
            _onlineTeams.value = emptyList()
            _onlineGames.value = emptyList()
            _isSearchingOnline.value = false
        }
    }

    fun selectDateAndJump(date: LocalDate) {
        selectDate(date)
        setSearchActive(false)
    }

    fun selectEventAndJump(event: GameEntity) {
        viewModelScope.launch {
            repository.saveEvent(event)
            try {
                val eventDate = LocalDate.parse(event.date)
                selectDateAndJump(eventDate)
            } catch (_: Exception) {
                setSearchActive(false)
            }
        }
    }

    fun previousWeek() {
        val newStart = _weekStartDate.value.minusWeeks(1)
        _weekStartDate.value = newStart
        _selectedDate.value = newStart
    }

    fun nextWeek() {
        val newStart = _weekStartDate.value.plusWeeks(1)
        _weekStartDate.value = newStart
        _selectedDate.value = newStart
    }

    fun goToToday() {
        val today = TimeUtils.currentCentralDate()
        _weekStartDate.value = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        _selectedDate.value = today
    }

    fun toggleFavorite(team: TeamEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(team, !team.isFavorite)
        }
    }

    fun toggleFilterOnlyFavorites() {
        _filterOnlyFavorites.value = !_filterOnlyFavorites.value
    }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            _isLoading.value = true
            val msg = repository.refreshSchedules(force = force)
            _statusMessage.value = msg
            _isLoading.value = false
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
