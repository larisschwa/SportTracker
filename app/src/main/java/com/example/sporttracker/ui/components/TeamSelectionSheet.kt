package com.example.sporttracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sporttracker.data.local.entity.TeamEntity

import com.example.sporttracker.util.SearchUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSelectionSheet(
    teams: List<TeamEntity>,
    onToggleFavorite: (TeamEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }
    var selectedSportFilter by remember { mutableStateOf<String?>(null) }

    val sportsList = remember(teams) {
        listOf("All") + teams.map { it.sport }.distinct().sorted()
    }

    val searchTerms = remember(searchQuery) {
        SearchUtils.getAssociatedTerms(searchQuery)
    }

    val filteredTeams = remember(teams, searchTerms, selectedSportFilter) {
        teams.filter { team ->
            val matchesSport = selectedSportFilter == null || selectedSportFilter == "All" || team.sport == selectedSportFilter
            val matchesSearch = searchTerms.isEmpty() || searchTerms.any { term ->
                team.name.contains(term, ignoreCase = true) ||
                team.abbreviation.contains(term, ignoreCase = true) ||
                team.league.contains(term, ignoreCase = true)
            }
            matchesSport && matchesSearch
        }
    }

    val groupedTeams = remember(filteredTeams) {
        filteredTeams.groupBy { "${it.sport} - ${it.league}" }
    }

    val favoriteCount = teams.count { it.isFavorite }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Choose Favorite Teams",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$favoriteCount selected across sports",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sport Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sportsList.forEach { sportName ->
                    val isSelected = (selectedSportFilter == null && sportName == "All") || selectedSportFilter == sportName
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedSportFilter = if (sportName == "All") null else sportName
                        },
                        label = { Text(sportName) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search teams, leagues...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (groupedTeams.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No teams matching \"$searchQuery\" in your saved list.\nUse the search bar on the main screen to search and add any team worldwide!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            // Grouped list by sport & league
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                groupedTeams.forEach { (groupTitle, divisionTeams) ->
                    item(key = "header_$groupTitle") {
                        Text(
                            text = groupTitle,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        )
                    }

                    items(divisionTeams, key = { it.id }) { team ->
                        TeamListItem(
                            team = team,
                            onToggle = { onToggleFavorite(team) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamListItem(
    team: TeamEntity,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 8.dp),
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
                modifier = Modifier.size(40.dp)
            ) {
                AsyncImage(
                    model = team.logoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = team.abbreviation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (team.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = if (team.isFavorite) "Remove Favorite" else "Add Favorite",
                tint = if (team.isFavorite) Color(0xFFFFB300) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
