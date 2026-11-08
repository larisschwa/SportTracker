package com.example.sporttracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sporttracker.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("""
        SELECT * FROM games 
        WHERE date = :date 
        ORDER BY timeCentral ASC, startTimeUtc ASC
    """)
    fun getGamesForDate(date: String): Flow<List<GameEntity>>

    @Query("""
        SELECT * FROM games 
        WHERE date = :date 
        AND (
            (isRace = 1 AND entityId IN (:entityIds)) OR 
            (isRace = 0 AND (
                homeTeamId IN (:entityIds) OR 
                awayTeamId IN (:entityIds) OR 
                homeTeamName IN (:teamNames) OR 
                awayTeamName IN (:teamNames)
            ))
        )
        ORDER BY timeCentral ASC, startTimeUtc ASC
    """)
    fun getGamesForFavoritesOnDate(
        entityIds: List<String>,
        teamNames: List<String>,
        date: String
    ): Flow<List<GameEntity>>

    @Query("""
        SELECT * FROM games 
        WHERE date BETWEEN :startDate AND :endDate 
        AND (
            (isRace = 1 AND entityId IN (:entityIds)) OR 
            (isRace = 0 AND (
                homeTeamId IN (:entityIds) OR 
                awayTeamId IN (:entityIds) OR 
                homeTeamName IN (:teamNames) OR 
                awayTeamName IN (:teamNames)
            ))
        )
        ORDER BY date ASC, timeCentral ASC, startTimeUtc ASC
    """)
    fun getGamesForFavoritesBetween(
        entityIds: List<String>,
        teamNames: List<String>,
        startDate: String,
        endDate: String
    ): Flow<List<GameEntity>>

    @Query("""
        SELECT * FROM games 
        WHERE date BETWEEN :startDate AND :endDate 
        ORDER BY date ASC, timeCentral ASC, startTimeUtc ASC
    """)
    fun getAllGamesBetween(
        startDate: String,
        endDate: String
    ): Flow<List<GameEntity>>

    @Query("""
        SELECT * FROM games 
        WHERE title LIKE '%' || :query || '%' 
        OR homeTeamName LIKE '%' || :query || '%' 
        OR awayTeamName LIKE '%' || :query || '%'
        OR league LIKE '%' || :query || '%'
        OR sport LIKE '%' || :query || '%'
        OR venue LIKE '%' || :query || '%'
        ORDER BY date ASC, timeCentral ASC
    """)
    fun searchGames(query: String): Flow<List<GameEntity>>

    @Query("""
        SELECT * FROM games 
        WHERE date >= :today
        AND (
            (isRace = 1 AND entityId IN (:entityIds)) OR 
            (isRace = 0 AND (
                homeTeamId IN (:entityIds) OR 
                awayTeamId IN (:entityIds) OR 
                homeTeamName IN (:teamNames) OR 
                awayTeamName IN (:teamNames)
            ))
        )
        ORDER BY date ASC, timeCentral ASC, startTimeUtc ASC 
        LIMIT 1
    """)
    suspend fun getNextUpcomingGame(
        entityIds: List<String>,
        teamNames: List<String>,
        today: String
    ): GameEntity?

    @Query("""
        SELECT * FROM games 
        WHERE date >= :today
        ORDER BY date ASC, timeCentral ASC, startTimeUtc ASC 
        LIMIT 1
    """)
    suspend fun getNextUpcomingGameAny(today: String): GameEntity?

    @Query("""
        DELETE FROM games 
        WHERE id LIKE 'sportsdb_%' 
        AND isRace = 0 
        AND homeTeamName NOT IN (:teamNames) 
        AND awayTeamName NOT IN (:teamNames)
    """)
    suspend fun purgeUnfollowedRemoteGames(teamNames: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getGameCount(): Int

    @Query("UPDATE games SET sport = 'Men''s Hockey' WHERE sport IN ('Ice Hockey', 'College Hockey')")
    suspend fun migrateHockeySport()

    @Query("DELETE FROM games WHERE id LIKE 'seed_%'")
    suspend fun clearSeedEvents()

    @Query("DELETE FROM games")
    suspend fun clearAll()
}
