package com.minecraftmodcreator.data.repository

import com.minecraftmodcreator.data.local.dao.ModDao
import com.minecraftmodcreator.data.repository.ModMapper.toDomain
import com.minecraftmodcreator.data.repository.ModMapper.toEntity
import com.minecraftmodcreator.domain.model.Mod
import com.minecraftmodcreator.domain.repository.ModRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModRepositoryImpl @Inject constructor(
    private val modDao: ModDao
) : ModRepository {

    override fun getAllMods(): Flow<List<Mod>> {
        return modDao.getAllMods().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRecentMods(limit: Int): Flow<List<Mod>> {
        return modDao.getRecentMods(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchMods(query: String): Flow<List<Mod>> {
        return modDao.searchMods(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getModCount(): Flow<Int> {
        return modDao.getModCount()
    }

    override suspend fun getModById(id: String): Mod? {
        return modDao.getModById(id)?.toDomain()
    }

    override suspend fun saveMod(mod: Mod) {
        val updatedMod = mod.copy(updatedAt = System.currentTimeMillis())
        modDao.insertMod(updatedMod.toEntity())
    }

    override suspend fun deleteMod(id: String) {
        modDao.deleteModById(id)
    }

    override suspend fun duplicateMod(id: String): Mod? {
        val original = getModById(id) ?: return null
        val duplicate = original.copy(
            id = UUID.randomUUID().toString(),
            name = "${original.name} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        saveMod(duplicate)
        return duplicate
    }
}
