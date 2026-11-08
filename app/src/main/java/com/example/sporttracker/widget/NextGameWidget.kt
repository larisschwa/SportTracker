package com.example.sporttracker.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.sporttracker.MainActivity
import com.example.sporttracker.data.local.AppDatabase
import com.example.sporttracker.data.local.entity.GameEntity
import com.example.sporttracker.util.TimeUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NextGameWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val db = AppDatabase.getInstance(context)
        val todayStr = TimeUtils.toCentralDateString(TimeUtils.currentCentralDate())

        val favoriteTeams = db.teamDao().getFavoriteTeamsList()
        val nextGame = if (favoriteTeams.isNotEmpty()) {
            val favIds = favoriteTeams.map { it.id }
            val favNames = favoriteTeams.map { it.name }
            db.gameDao().getNextUpcomingGame(favIds, favNames, todayStr)
                ?: db.gameDao().getNextUpcomingGameAny(todayStr)
        } else {
            db.gameDao().getNextUpcomingGameAny(todayStr)
        }

        provideContent {
            GlanceTheme {
                WidgetContent(context = context, game = nextGame)
            }
        }
    }

    @androidx.compose.runtime.Composable
    private fun WidgetContent(context: Context, game: GameEntity?) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .cornerRadius(16.dp)
                .padding(14.dp)
                .clickable(actionStartActivity(Intent(context, MainActivity::class.java))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Widget Header
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Next Event",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.primary
                        )
                    )

                    if (game != null && game.league.isNotEmpty()) {
                        Spacer(modifier = GlanceModifier.defaultWeight())
                        Text(
                            text = game.league,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = GlanceTheme.colors.outline
                            )
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                if (game != null) {
                    if (game.isRace) {
                        // Race display
                        Text(
                            text = game.title,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = GlanceTheme.colors.onSurface
                            ),
                            maxLines = 2
                        )
                    } else {
                        // Team matchup display: Away @ Home
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = game.awayTeamAbbrev.ifEmpty { game.awayTeamName.take(4).uppercase() },
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GlanceTheme.colors.onSurface
                                )
                            )

                            Text(
                                text = " @ ",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = GlanceTheme.colors.outline
                                )
                            )

                            Text(
                                text = game.homeTeamAbbrev.ifEmpty { game.homeTeamName.take(4).uppercase() },
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GlanceTheme.colors.onSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = GlanceModifier.height(6.dp))

                    // Date & Time in Central Time
                    val displayTime = formatGameDateAndCentralTime(game.date, game.timeCentral)
                    Text(
                        text = displayTime,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = GlanceTheme.colors.secondary
                        )
                    )

                    if (game.venue.isNotEmpty()) {
                        Spacer(modifier = GlanceModifier.height(2.dp))
                        Text(
                            text = game.venue,
                            style = TextStyle(
                                fontSize = 11.sp,
                                color = GlanceTheme.colors.outline
                            )
                        )
                    }
                } else {
                    Text(
                        text = "No upcoming events found.\nTap to select sports/teams.",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = GlanceTheme.colors.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }

    private fun formatGameDateAndCentralTime(dateStr: String, timeCentral: String): String {
        return try {
            val date = LocalDate.parse(dateStr)
            val dateFormatted = date.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
            if (timeCentral.isNotEmpty()) {
                "$dateFormatted • $timeCentral"
            } else {
                dateFormatted
            }
        } catch (_: Exception) {
            if (timeCentral.isNotEmpty()) "$dateStr • $timeCentral" else dateStr
        }
    }
}
