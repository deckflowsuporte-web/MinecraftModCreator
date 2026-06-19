package com.minecraftmodcreator.domain.repository

import com.minecraftmodcreator.domain.model.Mod
import kotlinx.coroutines.flow.Flow

interface ModRepository {
    fun getAllMods(): Flow<List<Mod>>
    fun getRecentMods(limit: Int): Flow<List<Mod>>
    fun searchMods(query: String): Flow<List<Mod>>
    fun getModCount(): Flow<Int>
    suspend fun getModById(id: String): Mod?
    suspend fun saveMod(mod: Mod)
    suspend fun deleteMod(id: String)
    suspend fun duplicateMod(id: String): Mod?
}
