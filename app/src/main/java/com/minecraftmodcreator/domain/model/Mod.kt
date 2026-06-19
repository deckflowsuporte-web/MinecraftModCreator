package com.minecraftmodcreator.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Mod(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val version: String = "1.0",
    val elements: List<ModElement> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
