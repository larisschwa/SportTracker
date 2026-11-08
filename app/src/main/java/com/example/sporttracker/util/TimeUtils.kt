package com.example.sporttracker.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeUtils {

    val CENTRAL_ZONE: ZoneId = ZoneId.of("America/Chicago")

    private val TIME_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("h:mm a 'CT'", Locale.US)

    private val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ISO_LOCAL_DATE

    fun currentCentralDate(): LocalDate = LocalDate.now(CENTRAL_ZONE)

    fun toCentralDateString(date: LocalDate): String = date.format(DATE_FORMATTER)

    /**
     * Converts a UTC ISO-8601 string (e.g. 2026-11-10T23:00:00Z) to Chicago Central Time.
     * Returns a formatted time string like "5:00 PM CT".
     */
    fun formatToCentralTime(utcTimestamp: String?, fallbackTime: String? = null): String {
        if (!utcTimestamp.isNullOrBlank()) {
            try {
                val instant = Instant.parse(utcTimestamp)
                val centralZoned = instant.atZone(CENTRAL_ZONE)
                return centralZoned.format(TIME_FORMATTER)
            } catch (_: Exception) {}
        }
        if (!fallbackTime.isNullOrBlank()) {
            return "$fallbackTime CT"
        }
        return "TBD CT"
    }

    /**
     * Determines the exact Central Time date for an event.
     * If an event is at 7:00 PM Central (which is 1:00 AM UTC the next day),
     * this returns the actual local Central calendar date.
     */
    fun getCentralLocalDate(utcTimestamp: String?, fallbackDateStr: String): LocalDate {
        if (!utcTimestamp.isNullOrBlank()) {
            try {
                val instant = Instant.parse(utcTimestamp)
                val centralZoned = instant.atZone(CENTRAL_ZONE)
                return centralZoned.toLocalDate()
            } catch (_: Exception) {}
        }
        return try {
            LocalDate.parse(fallbackDateStr)
        } catch (_: Exception) {
            currentCentralDate()
        }
    }
}
