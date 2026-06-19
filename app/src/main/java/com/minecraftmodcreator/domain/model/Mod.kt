package com.minecraftmodcreator.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Mod(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val version: String = "1.0.0",
    val author: String = "",
    
    // Mod Type
    val modType: ModType = ModType.BASIC,
    
    // Basic elements
    val elements: List<ModElement> = emptyList(),
    
    // Texture Mod specific
    val textures: List<TextureData> = emptyList(),
    
    // X-Ray Mod specific
    val xraySettings: XRaySettingsData? = null,
    
    // Glow Mod specific
    val glowConfigs: List<GlowConfigData> = emptyList(),
    
    // Interface Mod specific
    val uiScreens: List<UIScreenData> = emptyList(),
    
    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val iconColor: Long = 0xFF4CAF50,
    val iconData: String? = null,
    val templateId: String? = null,
    val status: ModStatus = ModStatus.DRAFT
)

@Serializable
enum class ModStatus {
    DRAFT,
    READY,
    EXPORTED
}

@Serializable
data class TextureData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val type: TextureType = TextureType.ITEM,
    val pixels: List<List<Long>> = List(16) { List(16) { 0L } }
)

@Serializable
enum class TextureType {
    ITEM, BLOCK, ENTITY, PARTICLE
}

@Serializable
data class XRaySettingsData(
    val enabled: Boolean = true,
    val range: Int = 16,
    val depth: Int = 64,
    val wireframeEnabled: Boolean = false,
    val showOresOnly: Boolean = false,
    val ores: List<OreData> = defaultOresData()
)

@Serializable
data class OreData(
    val name: String = "",
    val blockId: String = "",
    val color: Long = 0xFFFFFFFF,
    val visible: Boolean = true,
    val opacity: Float = 1f
)

fun defaultOresData() = listOf(
    OreData("Diamond", "minecraft:diamond_ore", 0xFF4FC3F7),
    OreData("Emerald", "minecraft:emerald_ore", 0xFF50FA7B),
    OreData("Gold", "minecraft:gold_ore", 0xFFFFD700),
    OreData("Iron", "minecraft:iron_ore", 0xFFBDB76B),
    OreData("Coal", "minecraft:coal_ore", 0xFF333333),
    OreData("Copper", "minecraft:copper_ore", 0xFFE87C5D),
    OreData("Lapis", "minecraft:lapis_ore", 0xFF3F51B5),
    OreData("Redstone", "minecraft:redstone_ore", 0xFFB71C1C),
    OreData("Ancient Debris", "minecraft:ancient_debris", 0xFF8B4513)
)

@Serializable
data class GlowConfigData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val target: GlowTargetData = GlowTargetData.ALL,
    val targetId: String = "",
    val colorType: GlowColorTypeData = GlowColorTypeData.FIXED,
    val fixedColor: Long = 0xFFFFFF00,
    val intensity: Float = 1f,
    val enabled: Boolean = true
)

@Serializable
enum class GlowTargetData {
    MOBS, BLOCKS, ITEMS, ALL
}

@Serializable
enum class GlowColorTypeData {
    FIXED, RAINBOW, PULSING, BREATHING
}

@Serializable
data class UIScreenData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val elements: List<UIElementData> = emptyList(),
    val backgroundColor: Long = 0x80000000,
    val width: Float = 300f,
    val height: Float = 400f
)

@Serializable
data class UIElementData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: UIElementTypeData = UIElementTypeData.BUTTON,
    var x: Float = 100f,
    var y: Float = 100f,
    var width: Float = 100f,
    var height: Float = 50f,
    var text: String = "",
    var backgroundColor: Long = 0xFF4CAF50,
    var textColor: Long = 0xFFFFFFFF,
    var visible: Boolean = true,
    var action: String = ""
)

@Serializable
enum class UIElementTypeData {
    BUTTON, LABEL, IMAGE, PROGRESS_BAR, SLOT, TOGGLE, TEXT_INPUT, SCROLL_PANEL, RECTANGLE, ICON
}
