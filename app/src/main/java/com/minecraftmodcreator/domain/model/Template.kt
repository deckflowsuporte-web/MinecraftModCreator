package com.minecraftmodcreator.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class TemplateCategory {
    DECORATION,
    UTILITY,
    ADVENTURE,
    CHALLENGE
}

@Serializable
data class Template(
    val id: String,
    val name: String,
    val description: String,
    val category: TemplateCategory,
    val elements: List<ModElement>
)
