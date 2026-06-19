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
    
    // Action Mod - Logic/Behavior mods
    val modActions: List<ModActionData> = emptyList(),
    
    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val iconColor: Long = 0xFF4CAF50,
    val iconData: String? = null,
    val templateId: String? = null,
    val status: ModStatus = ModStatus.DRAFT
)

// ============== ACTION SYSTEM FOR LOGIC MODS ==============
@Serializable
data class ModActionData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val trigger: TriggerTypeData = TriggerTypeData.PLAYER_SNEAK,
    val conditions: List<ConditionData> = emptyList(),
    val actions: List<ActionStepData> = emptyList(),
    val enabled: Boolean = true
)

@Serializable
enum class TriggerTypeData {
    // Player Actions
    PLAYER_SNEAK, PLAYER_SPRINT, PLAYER_JUMP, PLAYER_HURT, PLAYER_DEATH, PLAYER_RESPAWN, PLAYER_LEVEL_UP,
    
    // Item Actions
    ITEM_USE, ITEM_HOLD, ITEM_SWAP, ITEM_DROP, ITEM_BREAK,
    
    // Block Actions
    BLOCK_BREAK, BLOCK_PLACE, BLOCK_INTERACT, BLOCK_STEP,
    
    // Entity Actions
    ENTITY_KILL, ENTITY_SPAWN, ENTITY_DAMAGE, ENTITY_INTERACT,
    
    // World Actions
    WORLD_TIME, WORLD_ENTER, WORLD_CHAT, WORLD_COMMAND,
    
    // Combat
    PLAYER_ATTACK, PLAYER_BLOCK,
    
    // Inventory
    INVENTORY_OPEN, INVENTORY_CLOSE, CRAFT_ITEM
}

@Serializable
data class ConditionData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val conditionType: ConditionTypeData = ConditionTypeData.HAS_ITEM,
    val value: String = "",
    val negate: Boolean = false
)

@Serializable
enum class ConditionTypeData {
    HAS_ITEM, HAS_XP, HAS_EFFECT, HEALTH_ABOVE, HEALTH_BELOW,
    IN_BIOME, TIME_OF_DAY, IS_SNEAKING, IS_SPRINTING,
    INVENTORY_FULL, BLOCK_ABOVE, TOOL_BROKEN, PLAYER_HURT, RANDOM_CHANCE
}

@Serializable
data class ActionStepData(
    val id: String = java.util.UUID.randomUUID().toString(),
    val actionType: ActionTypeData = ActionTypeData.GIVE_ITEM,
    val target: String = "@s",
    val params: Map<String, String> = emptyMap()
)

@Serializable
enum class ActionTypeData {
    // Item Actions
    GIVE_ITEM, REMOVE_ITEM, MULTIPLY_ITEM, SET_ITEM,
    
    // Entity Actions
    SPAWN_ENTITY, KILL_ENTITY, TELEPORT_PLAYER, TELEPORT_ENTITY,
    
    // Effects
    APPLY_EFFECT, REMOVE_EFFECT, HEAL, DAMAGE,
    
    // World Actions
    SET_BLOCK, REMOVE_BLOCK, EXECUTE_COMMAND, BROADCAST_MESSAGE,
    
    // Player Actions
    SET_HEALTH, SET_FOOD, SET_GAMEMODE, SET_EXP,
    
    // Sound & Visual
    PLAY_SOUND, SPAWN_PARTICLE,
    
    // Logic
    DELAY, LOOP, CONDITION
}

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
