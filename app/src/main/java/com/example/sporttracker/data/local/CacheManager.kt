package com.example.sporttracker.data.local

import android.content.Context
import android.content.SharedPreferences

class CacheManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("api_cache_control", Context.MODE_PRIVATE)

    companion object {
        // Only refresh a team's schedule once every 6 hours automatically
        private const val CACHE_EXPIRY_MS = 6 * 60 * 60 * 1000L

        // Minimum cooldown between manual refreshes (10 minutes)
        private const val MANUAL_REFRESH_COOLDOWN_MS = 10 * 60 * 1000L

        // Daily maximum API requests safeguard (Free tier limit is 100, we cap at 50)
        private const val MAX_DAILY_REQUESTS = 50
    }

    /**
     * Checks whether an API request is permitted for the given team ID.
     */
    fun shouldFetchForTeam(teamId: String, force: Boolean = false): Boolean {
        if (isDailyBudgetExceeded()) return false

        val lastFetch = prefs.getLong("last_fetch_$teamId", 0L)
        val now = System.currentTimeMillis()

        return if (force) {
            now - lastFetch >= MANUAL_REFRESH_COOLDOWN_MS
        } else {
            now - lastFetch >= CACHE_EXPIRY_MS
        }
    }

    /**
     * Records that an API call was made for the given team.
     */
    fun recordFetch(teamId: String) {
        val now = System.currentTimeMillis()
        val editor = prefs.edit()
        editor.putLong("last_fetch_$teamId", now)
        editor.putLong("last_manual_refresh", now)

        // Increment daily count
        val todayKey = getTodayDateKey(now)
        val currentCount = prefs.getInt(todayKey, 0)
        editor.putInt(todayKey, currentCount + 1)
        editor.apply()
    }

    /**
     * Returns true if the daily request budget has been reached.
     */
    fun isDailyBudgetExceeded(): Boolean {
        val todayKey = getTodayDateKey(System.currentTimeMillis())
        val count = prefs.getInt(todayKey, 0)
        return count >= MAX_DAILY_REQUESTS
    }

    /**
     * Returns the remaining number of requests allowed for today.
     */
    fun getRemainingDailyBudget(): Int {
        val todayKey = getTodayDateKey(System.currentTimeMillis())
        val count = prefs.getInt(todayKey, 0)
        return (MAX_DAILY_REQUESTS - count).coerceAtLeast(0)
    }

    private fun getTodayDateKey(timestamp: Long): String {
        val dayIndex = timestamp / (24 * 60 * 60 * 1000L)
        return "requests_day_$dayIndex"
    }
}
