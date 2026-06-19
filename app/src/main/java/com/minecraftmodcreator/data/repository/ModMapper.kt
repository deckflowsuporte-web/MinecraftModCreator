package com.minecraftmodcreator.data.repository

import com.minecraftmodcreator.data.local.entity.ModEntity
import com.minecraftmodcreator.domain.model.Mod
import com.minecraftmodcreator.domain.model.ModElement
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ModMapper {
    private val json = Json { ignoreUnknownKeys = true }

    fun ModEntity.toDomain(): Mod {
        val elements = try {
            json.decodeFromString<List<ModElement>>(elementsJson)
        } catch (e: Exception) {
            emptyList()
        }
        return Mod(
            id = id,
            name = name,
            description = description,
            version = version,
            elements = elements,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    fun Mod.toEntity(): ModEntity {
        return ModEntity(
            id = id,
            name = name,
            description = description,
            version = version,
            elementsJson = json.encodeToString(elements),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
