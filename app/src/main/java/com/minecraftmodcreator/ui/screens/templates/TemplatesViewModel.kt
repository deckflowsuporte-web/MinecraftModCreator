package com.minecraftmodcreator.ui.screens.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraftmodcreator.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class TemplatesUiState(
    val isLoading: Boolean = true,
    val templates: List<Template> = emptyList(),
    val selectedCategory: TemplateCategory? = null
)

@Singleton
class TemplateRepository @Inject constructor() {
    private val templates = listOf(
        Template(
            id = "decoration_lights",
            name = "Decorative Lights",
            description = "Create colorful light blocks for decoration",
            category = TemplateCategory.DECORATION,
            elements = listOf(
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.BLOCK,
                    name = "Red Light Block",
                    properties = mapOf("resistance" to "3.0", "hardness" to "0.5", "tool" to "none")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.BLOCK,
                    name = "Blue Light Block",
                    properties = mapOf("resistance" to "3.0", "hardness" to "0.5", "tool" to "none")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.BLOCK,
                    name = "Green Light Block",
                    properties = mapOf("resistance" to "3.0", "hardness" to "0.5", "tool" to "none")
                )
            )
        ),
        Template(
            id = "utility_tools",
            name = "Super Tools",
            description = "Ultimate tools that never break",
            category = TemplateCategory.UTILITY,
            elements = listOf(
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.TOOL,
                    name = "Super Pickaxe",
                    properties = mapOf("toolType" to "pickaxe", "material" to "diamond", "durability" to "9999", "damage" to "5.0")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.TOOL,
                    name = "Super Axe",
                    properties = mapOf("toolType" to "axe", "material" to "diamond", "durability" to "9999", "damage" to "7.0")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.TOOL,
                    name = "Super Sword",
                    properties = mapOf("toolType" to "sword", "material" to "diamond", "durability" to "9999", "damage" to "12.0")
                )
            )
        ),
        Template(
            id = "food_pack",
            name = "Gourmet Food Pack",
            description = "Delicious new foods with special effects",
            category = TemplateCategory.ADVENTURE,
            elements = listOf(
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ITEM,
                    name = "Golden Apple Plus",
                    properties = mapOf("isEdible" to "true", "foodPoints" to "10", "saturation" to "5.0")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ITEM,
                    name = "Magic Cookie",
                    properties = mapOf("isEdible" to "true", "foodPoints" to "4", "saturation" to "2.0")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ITEM,
                    name = "Speed Potion",
                    properties = mapOf("isEdible" to "false", "description" to "Grants speed boost")
                )
            )
        ),
        Template(
            id = "boss_mobs",
            name = "Epic Bosses",
            description = "Challenging boss mobs for hardcore players",
            category = TemplateCategory.CHALLENGE,
            elements = listOf(
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.MOB,
                    name = "Shadow Dragon",
                    properties = mapOf("health" to "200", "damage" to "15", "isAggressive" to "true", "behavior" to "attack")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.MOB,
                    name = "Ice Golem",
                    properties = mapOf("health" to "80", "damage" to "10", "isAggressive" to "false", "behavior" to "wander")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.MOB,
                    name = "Fire Elemental",
                    properties = mapOf("health" to "100", "damage" to "12", "isAggressive" to "true", "behavior" to "follow")
                )
            )
        ),
        Template(
            id = "magic_armor",
            name = "Enchanted Armor Set",
            description = "Magical armor with special abilities",
            category = TemplateCategory.UTILITY,
            elements = listOf(
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ARMOR,
                    name = "Magic Helmet",
                    properties = mapOf("armorPart" to "helmet", "material" to "diamond", "protection" to "4", "durability" to "500")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ARMOR,
                    name = "Magic Chestplate",
                    properties = mapOf("armorPart" to "chestplate", "material" to "diamond", "protection" to "8", "durability" to "500")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ARMOR,
                    name = "Magic Leggings",
                    properties = mapOf("armorPart" to "leggings", "material" to "diamond", "protection" to "6", "durability" to "500")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ARMOR,
                    name = "Magic Boots",
                    properties = mapOf("armorPart" to "boots", "material" to "diamond", "protection" to "4", "durability" to "500")
                )
            )
        ),
        Template(
            id = "ores_plus",
            name = "Extra Ores",
            description = "New rare ores for mining",
            category = TemplateCategory.DECORATION,
            elements = listOf(
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.BLOCK,
                    name = "Ruby Ore",
                    properties = mapOf("resistance" to "15.0", "hardness" to "5.0", "tool" to "pickaxe")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ITEM,
                    name = "Ruby",
                    properties = mapOf("stackSize" to "64", "description" to "A precious gem")
                ),
                ModElement(
                    id = UUID.randomUUID().toString(),
                    type = ModElementType.ITEM,
                    name = "Sapphire",
                    properties = mapOf("stackSize" to "64", "description" to "A beautiful blue gem")
                )
            )
        )
    )

    fun getAllTemplates(): List<Template> = templates

    fun getTemplatesByCategory(category: TemplateCategory): List<Template> {
        return templates.filter { it.category == category }
    }

    fun getTemplateById(id: String): Template? {
        return templates.find { it.id == id }
    }
}

@HiltViewModel
class TemplatesViewModel @Inject constructor(
    private val templateRepository: TemplateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemplatesUiState())
    val uiState: StateFlow<TemplatesUiState> = _uiState.asStateFlow()

    init {
        loadTemplates()
    }

    private fun loadTemplates() {
        viewModelScope.launch {
            _uiState.value = TemplatesUiState(
                isLoading = false,
                templates = templateRepository.getAllTemplates()
            )
        }
    }

    fun filterByCategory(category: TemplateCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }
}
