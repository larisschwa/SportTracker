package com.example.sporttracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sporttracker.data.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams ORDER BY sport ASC, name ASC")
    fun getAllTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE sport = :sport ORDER BY name ASC")
    fun getTeamsBySport(sport: String): Flow<List<TeamEntity>>

    @Query("SELECT DISTINCT sport FROM teams ORDER BY sport ASC")
    fun getDistinctSports(): Flow<List<String>>

    @Query("SELECT * FROM teams WHERE isFavorite = 1 ORDER BY sport ASC, name ASC")
    fun getFavoriteTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE isFavorite = 1 AND sport = :sport ORDER BY name ASC")
    fun getFavoriteTeamsBySport(sport: String): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE isFavorite = 1")
    suspend fun getFavoriteTeamsList(): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE id = :teamId LIMIT 1")
    suspend fun getTeamById(teamId: String): TeamEntity?

    @Query("UPDATE teams SET isFavorite = :isFavorite WHERE id = :teamId")
    suspend fun updateFavorite(teamId: String, isFavorite: Boolean)

    @Query("UPDATE teams SET sport = 'Men''s Hockey' WHERE sport IN ('Ice Hockey', 'College Hockey')")
    suspend fun migrateHockeySport()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(team: TeamEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(teams: List<TeamEntity>)

    @Query("""
        SELECT * FROM teams 
        WHERE name LIKE '%' || :query || '%' 
        OR abbreviation LIKE '%' || :query || '%'
        OR league LIKE '%' || :query || '%'
        OR sport LIKE '%' || :query || '%'
        ORDER BY isFavorite DESC, name ASC
    """)
    fun searchTeams(query: String): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE LOWER(name) = LOWER(:name) LIMIT 1")
    suspend fun getTeamByName(name: String): TeamEntity?

    @Query("DELETE FROM teams WHERE LOWER(name) = LOWER(:name) AND id != :keepId")
    suspend fun deleteDuplicateTeamsByName(name: String, keepId: String)

    @Query("UPDATE teams SET logoUrl = :logoUrl WHERE LOWER(name) = LOWER(:name) AND (logoUrl IS NULL OR logoUrl = '')")
    suspend fun updateMissingLogo(name: String, logoUrl: String)

    @Query("SELECT COUNT(*) FROM teams")
    suspend fun getTeamCount(): Int
}
