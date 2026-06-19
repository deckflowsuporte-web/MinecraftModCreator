package com.minecraftmodcreator.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ModElement(
    val id: String,
    val type: ModElementType,
    val name: String,
    val properties: Map<String, String> = emptyMap()
)

@Serializable
data class ItemProperties(
    val stackSize: Int = 64,
    val isEdible: Boolean = false,
    val foodPoints: Int = 0,
    val saturation: Float = 0f
)

@Serializable
data class BlockProperties(
    val resistance: Float = 0f,
    val requiredTool: String = "pickaxe",
    val hardness: Float = 0f,
    val drops: List<String> = emptyList()
)

@Serializable
data class ToolProperties(
    val toolType: ToolType = ToolType.PICKAXE,
    val material: ToolMaterial = ToolMaterial.STONE,
    val durability: Int = 100,
    val miningSpeed: Float = 1f,
    val damage: Float = 1f
)

@Serializable
enum class ToolType {
    PICKAXE, AXE, SHOVEL, HOE, SWORD
}

@Serializable
enum class ToolMaterial {
    WOOD, STONE, IRON, GOLD, DIAMOND, NETHERITE
}

@Serializable
data class ArmorProperties(
    val armorPart: ArmorPart = ArmorPart.HELMET,
    val material: ToolMaterial = ToolMaterial.IRON,
    val protection: Int = 2,
    val durability: Int = 100
)

@Serializable
enum class ArmorPart {
    HELMET, CHESTPLATE, LEGGINGS, BOOTS
}

@Serializable
data class MobProperties(
    val health: Float = 20f,
    val damage: Float = 3f,
    val isAggressive: Boolean = false,
    val behavior: String = "idle"
)

@Serializable
data class RecipeProperties(
    val resultItem: String = "",
    val resultAmount: Int = 1,
    val ingredients: List<String> = emptyList(),
    val recipeType: RecipeType = RecipeType.CRAFTING_SHAPED
)

@Serializable
enum class RecipeType {
    CRAFTING_SHAPED, CRAFTING_SHAPELESS, SMELTING
}
