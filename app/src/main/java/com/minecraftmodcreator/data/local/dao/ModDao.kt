package com.minecraftmodcreator.data.local.dao

import androidx.room.*
import com.minecraftmodcreator.data.local.entity.ModEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModDao {
    @Query("SELECT * FROM mods ORDER BY updatedAt DESC")
    fun getAllMods(): Flow<List<ModEntity>>

    @Query("SELECT * FROM mods WHERE id = :id")
    suspend fun getModById(id: String): ModEntity?

    @Query("SELECT * FROM mods ORDER BY updatedAt DESC LIMIT :limit")
    fun getRecentMods(limit: Int): Flow<List<ModEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMod(mod: ModEntity)

    @Update
    suspend fun updateMod(mod: ModEntity)

    @Delete
    suspend fun deleteMod(mod: ModEntity)

    @Query("DELETE FROM mods WHERE id = :id")
    suspend fun deleteModById(id: String)

    @Query("SELECT COUNT(*) FROM mods")
    fun getModCount(): Flow<Int>

    @Query("SELECT * FROM mods WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchMods(query: String): Flow<List<ModEntity>>
}
