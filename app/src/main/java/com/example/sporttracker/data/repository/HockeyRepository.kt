package com.example.sporttracker.data.repository

import android.content.Context
import com.example.sporttracker.data.local.dao.GameDao
import com.example.sporttracker.data.local.dao.TeamDao

/**
 * Backward compatibility alias for SportsRepository.
 */
class HockeyRepository(
    context: Context,
    teamDao: TeamDao,
    gameDao: GameDao
) : SportsRepository(context, teamDao, gameDao)
