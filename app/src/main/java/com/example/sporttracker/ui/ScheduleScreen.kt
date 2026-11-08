package com.example.sporttracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sporttracker.data.local.entity.GameEntity
import com.example.sporttracker.data.local.entity.TeamEntity
import com.example.sporttracker.ui.components.GameCard
import com.example.sporttracker.ui.components.TeamSelectionSheet
import com.example.sporttracker.ui.components.WeekCalendarBar
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: SportsViewModel = viewModel(),
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val weekStartDate by viewModel.weekStartDate.collectAsState()
    val games by viewModel.gamesForSelectedDate.collectAsState()
    val weekGamesCount by viewModel.weekGamesCount.collectAsState()
    val allTeams by viewModel.allTeams.collectAsState()
    val favoriteTeams by viewModel.favoriteTeams.collectAsState()
    val availableSports by viewModel.availableSports.collectAsState()
    val selectedSport by viewModel.selectedSport.collectAsState()
    val filterOnlyFavorites by viewModel.filterOnlyFavorites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val isSearchingOnline by viewModel.isSearchingOnline.collectAsState()
    val searchResultsGames by viewModel.searchResultsGames.collectAsState()
    val searchResultsTeams by viewModel.searchResultsTeams.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showTeamSheet by remember { mutableStateOf(false) }

    LaunchedEffect(statusMessage) {
        statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    val formattedSelectedDate = remember(selectedDate) {
        selectedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (isSearchActive) {
                // Interactive Search TopBar
                TopAppBar(
                    title = {
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Search events, teams, races...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.setSearchActive(false) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Close Search"
                            )
                        }
                    },
                    actions = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Search")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            } else {
                // Standard TopAppBar
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "SportTracker",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (filterOnlyFavorites && favoriteTeams.isNotEmpty()) {
                                    "${favoriteTeams.size} Followed • Central Time (CT)"
                                } else {
                                    "${selectedSport ?: "All Events"} • Central Time (CT)"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        // Search Button
                        IconButton(onClick = { viewModel.setSearchActive(true) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search events and teams"
                            )
                        }

                        // Filter favorites toggle
                        IconButton(onClick = { viewModel.toggleFilterOnlyFavorites() }) {
                            Icon(
                                imageVector = if (filterOnlyFavorites) Icons.Filled.FilterAlt else Icons.Filled.FilterAltOff,
                                contentDescription = "Toggle Filter",
                                tint = if (filterOnlyFavorites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Open team selection sheet
                        IconButton(onClick = { showTeamSheet = true }) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Select Favorite Teams",
                                tint = if (favoriteTeams.isNotEmpty()) Color(0xFFFFB300) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Refresh
                        IconButton(onClick = { viewModel.refresh(force = true) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Schedule"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    ) { innerPadding ->
        if (isSearchActive) {
            // Search Results View
            SearchResultsView(
                query = searchQuery,
                matchingEvents = searchResultsGames,
                matchingTeams = searchResultsTeams,
                isSearchingOnline = isSearchingOnline,
                onEventClicked = { event ->
                    viewModel.selectEventAndJump(event)
                },
                onToggleFavoriteTeam = { team ->
                    viewModel.toggleFavorite(team)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            // Main Week Schedule View
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Sports Filter Bar
                if (availableSports.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedSport == null,
                            onClick = { viewModel.selectSport(null) },
                            label = { Text("All Followed") }
                        )

                        availableSports.forEach { sportName ->
                            FilterChip(
                                selected = selectedSport == sportName,
                                onClick = {
                                    viewModel.selectSport(if (selectedSport == sportName) null else sportName)
                                },
                                label = { Text(sportName) }
                            )
                        }
                    }
                }

                // Horizontal Week Bar (Monday - Sunday)
                WeekCalendarBar(
                    weekStartDate = weekStartDate,
                    selectedDate = selectedDate,
                    gamesCountByDate = weekGamesCount,
                    onDateSelected = { viewModel.selectDate(it) },
                    onPreviousWeek = { viewModel.previousWeek() },
                    onNextWeek = { viewModel.nextWeek() },
                    onGoToToday = { viewModel.goToToday() }
                )

                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                // Date & Game count banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$formattedSelectedDate (CT)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "${games.size} ${if (games.size == 1) "Event" else "Events"}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Events List or Empty State
                if (games.isEmpty()) {
                    EmptyStateView(
                        filterOnlyFavorites = filterOnlyFavorites,
                        favoriteCount = favoriteTeams.size,
                        selectedSport = selectedSport,
                        onOpenTeamSelector = { showTeamSheet = true },
                        onShowAllGames = { viewModel.toggleFilterOnlyFavorites() }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(games, key = { it.id }) { game ->
                            GameCard(game = game)
                        }
                    }
                }
            }
        }
    }

    if (showTeamSheet) {
        TeamSelectionSheet(
            teams = allTeams,
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onDismiss = { showTeamSheet = false }
        )
    }
}

@Composable
private fun SearchResultsView(
    query: String,
    matchingEvents: List<GameEntity>,
    matchingTeams: List<TeamEntity>,
    isSearchingOnline: Boolean,
    onEventClicked: (GameEntity) -> Unit,
    onToggleFavoriteTeam: (TeamEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (query.isBlank()) {
        Box(
            modifier = modifier.padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Type any team name (e.g. \"Gophers\", \"Wild\", \"Vikings\", \"Lakers\"), sport, or event name to search.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    if (matchingEvents.isEmpty() && matchingTeams.isEmpty()) {
        Box(
            modifier = modifier.padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSearchingOnline) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Searching online for \"$query\"...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = "No events or teams found for \"$query\".",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    Column(modifier = modifier) {
        if (isSearchingOnline) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
        // Teams & Series section
        if (matchingTeams.isNotEmpty()) {
            item {
                Text(
                    text = "Teams & Series",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            items(matchingTeams, key = { "team_${it.id}" }) { team ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onToggleFavoriteTeam(team) }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(36.dp)
                        ) {
                            if (team.logoUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = team.logoUrl,
                                    contentDescription = null,
                                    modifier = Modifier.padding(4.dp).clip(CircleShape),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = team.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${team.sport} • ${team.league}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = { onToggleFavoriteTeam(team) }) {
                        Icon(
                            imageVector = if (team.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = if (team.isFavorite) Color(0xFFFFB300) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Events section
        if (matchingEvents.isNotEmpty()) {
            item {
                Text(
                    text = "Scheduled Events & Races (Tap to jump to date)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
                )
            }

            items(matchingEvents, key = { "event_${it.id}" }) { event ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onEventClicked(event) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${event.date} • ${event.timeCentral}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = event.league,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (event.venue.isNotEmpty()) {
                            Text(
                                text = event.venue,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
private fun EmptyStateView(
    filterOnlyFavorites: Boolean,
    favoriteCount: Int,
    selectedSport: String?,
    onOpenTeamSelector: () -> Unit,
    onShowAllGames: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.EventBusy,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Events Scheduled",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (filterOnlyFavorites && (favoriteCount == 0)) {
                    "You haven't selected any favorite teams or races yet.\nFollow the Wild, Gophers, Supercross, and more!"
                } else if (filterOnlyFavorites) {
                    "None of your followed ${selectedSport ?: "sports/races"} have events on this date."
                } else {
                    "There are no ${selectedSport ?: ""} events scheduled for this date."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (filterOnlyFavorites && favoriteCount == 0) {
                Button(
                    onClick = onOpenTeamSelector,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Select Teams & Races")
                }
            } else if (filterOnlyFavorites) {
                Button(
                    onClick = onShowAllGames,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View All Events")
                }
            }
        }
    }
}
