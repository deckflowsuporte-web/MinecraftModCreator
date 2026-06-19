package com.minecraftmodcreator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mods")
data class ModEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val elementsJson: String,
    val createdAt: Long,
    val updatedAt: Long
)
