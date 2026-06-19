package com.minecraftmodcreator.ui.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraftmodcreator.domain.model.*
import com.minecraftmodcreator.domain.repository.ModRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ElementEditorUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val element: ModElement? = null,
    val modName: String = "",
    val saveSuccess: Boolean = false
)

@HiltViewModel
class ElementEditorViewModel @Inject constructor(
    private val modRepository: ModRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val modId: String = savedStateHandle.get<String>("modId") ?: ""
    private val elementId: String? = savedStateHandle.get<String>("elementId")?.takeIf { it != "new" }

    private val _uiState = MutableStateFlow(ElementEditorUiState())
    val uiState: StateFlow<ElementEditorUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val mod = modRepository.getModById(modId)
            if (mod != null) {
                val element = mod.elements.find { it.id == elementId }
                _uiState.value = ElementEditorUiState(
                    isLoading = false,
                    element = element,
                    modName = mod.name
                )
            }
        }
    }

    fun updateElementName(name: String) {
        _uiState.value.element?.let { current ->
            _uiState.value = _uiState.value.copy(
                element = current.copy(name = name)
            )
        }
    }

    fun updateProperty(key: String, value: String) {
        _uiState.value.element?.let { current ->
            val newProps = current.properties.toMutableMap()
            newProps[key] = value
            _uiState.value = _uiState.value.copy(
                element = current.copy(properties = newProps)
            )
        }
    }

    fun saveElement() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            try {
                val mod = modRepository.getModById(modId)
                val currentElement = _uiState.value.element
                if (mod != null && currentElement != null) {
                    val updatedElements = mod.elements.toMutableList()
                    val index = updatedElements.indexOfFirst { it.id == currentElement.id }
                    if (index != -1) {
                        updatedElements[index] = currentElement
                    } else {
                        updatedElements.add(currentElement)
                    }
                    val updatedMod = mod.copy(
                        elements = updatedElements,
                        updatedAt = System.currentTimeMillis()
                    )
                    modRepository.saveMod(updatedMod)
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementEditorScreen(
    modId: String,
    elementId: String?,
    onNavigateBack: () -> Unit,
    viewModel: ElementEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.element?.type?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Element") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveElement() },
                        enabled = !uiState.isSaving && uiState.element != null
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(Icons.Default.Save, contentDescription = "Save")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.element?.let { element ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Element Type Header
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = getElementEmoji(element.type),
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = element.type.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Mod: ${uiState.modName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // Common Properties
                    ElementPropertiesSection(
                        element = element,
                        onNameChange = viewModel::updateElementName,
                        onPropertyChange = viewModel::updateProperty
                    )
                }
            }
        }
    }
}

@Composable
private fun ElementPropertiesSection(
    element: ModElement,
    onNameChange: (String) -> Unit,
    onPropertyChange: (String, String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Properties",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = element.name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            when (element.type) {
                ModElementType.ITEM -> ItemPropertiesEditor(element, onPropertyChange)
                ModElementType.BLOCK -> BlockPropertiesEditor(element, onPropertyChange)
                ModElementType.TOOL -> ToolPropertiesEditor(element, onPropertyChange)
                ModElementType.ARMOR -> ArmorPropertiesEditor(element, onPropertyChange)
                ModElementType.MOB -> MobPropertiesEditor(element, onPropertyChange)
                ModElementType.RECIPE -> RecipePropertiesEditor(element, onPropertyChange)
            }
        }
    }
}

@Composable
private fun ItemPropertiesEditor(
    element: ModElement,
    onPropertyChange: (String, String) -> Unit
) {
    val stackSize = element.properties["stackSize"]?.toIntOrNull() ?: 64

    PropertyTextField(
        value = element.properties["description"] ?: "",
        onValueChange = { onPropertyChange("description", it) },
        label = "Description"
    )

    PropertyNumberField(
        value = stackSize.toString(),
        onValueChange = { onPropertyChange("stackSize", it) },
        label = "Stack Size (1-64)"
    )

    PropertySwitch(
        checked = element.properties["isEdible"]?.toBoolean() ?: false,
        onCheckedChange = { onPropertyChange("isEdible", it.toString()) },
        label = "Edible"
    )
}

@Composable
private fun BlockPropertiesEditor(
    element: ModElement,
    onPropertyChange: (String, String) -> Unit
) {
    val resistance = element.properties["resistance"]?.toFloatOrNull() ?: 0f
    val hardness = element.properties["hardness"]?.toFloatOrNull() ?: 0f

    PropertyNumberField(
        value = resistance.toString(),
        onValueChange = { onPropertyChange("resistance", it) },
        label = "Resistance (blast resistance)"
    )

    PropertyNumberField(
        value = hardness.toString(),
        onValueChange = { onPropertyChange("hardness", it) },
        label = "Hardness"
    )

    PropertyDropdown(
        value = element.properties["tool"] ?: "pickaxe",
        onValueChange = { onPropertyChange("tool", it) },
        label = "Required Tool",
        options = listOf("pickaxe", "axe", "shovel", "hoe", "none")
    )
}

@Composable
private fun ToolPropertiesEditor(
    element: ModElement,
    onPropertyChange: (String, String) -> Unit
) {
    val durability = element.properties["durability"]?.toIntOrNull() ?: 100
    val damage = element.properties["damage"]?.toFloatOrNull() ?: 1f

    PropertyDropdown(
        value = element.properties["toolType"] ?: "sword",
        onValueChange = { onPropertyChange("toolType", it) },
        label = "Tool Type",
        options = listOf("sword", "pickaxe", "axe", "shovel", "hoe")
    )

    PropertyDropdown(
        value = element.properties["material"] ?: "stone",
        onValueChange = { onPropertyChange("material", it) },
        label = "Material",
        options = listOf("wood", "stone", "iron", "gold", "diamond", "netherite")
    )

    PropertyNumberField(
        value = durability.toString(),
        onValueChange = { onPropertyChange("durability", it) },
        label = "Durability"
    )

    PropertyNumberField(
        value = damage.toString(),
        onValueChange = { onPropertyChange("damage", it) },
        label = "Damage"
    )
}

@Composable
private fun ArmorPropertiesEditor(
    element: ModElement,
    onPropertyChange: (String, String) -> Unit
) {
    val protection = element.properties["protection"]?.toIntOrNull() ?: 2
    val durability = element.properties["durability"]?.toIntOrNull() ?: 100

    PropertyDropdown(
        value = element.properties["armorPart"] ?: "helmet",
        onValueChange = { onPropertyChange("armorPart", it) },
        label = "Armor Part",
        options = listOf("helmet", "chestplate", "leggings", "boots")
    )

    PropertyDropdown(
        value = element.properties["material"] ?: "iron",
        onValueChange = { onPropertyChange("material", it) },
        label = "Material",
        options = listOf("leather", "chainmail", "iron", "gold", "diamond", "netherite")
    )

    PropertyNumberField(
        value = protection.toString(),
        onValueChange = { onPropertyChange("protection", it) },
        label = "Protection (armor points)"
    )

    PropertyNumberField(
        value = durability.toString(),
        onValueChange = { onPropertyChange("durability", it) },
        label = "Durability"
    )
}

@Composable
private fun MobPropertiesEditor(
    element: ModElement,
    onPropertyChange: (String, String) -> Unit
) {
    val health = element.properties["health"]?.toFloatOrNull() ?: 20f
    val damage = element.properties["damage"]?.toFloatOrNull() ?: 3f

    PropertyNumberField(
        value = health.toString(),
        onValueChange = { onPropertyChange("health", it) },
        label = "Health (hearts)"
    )

    PropertyNumberField(
        value = damage.toString(),
        onValueChange = { onPropertyChange("damage", it) },
        label = "Attack Damage"
    )

    PropertySwitch(
        checked = element.properties["isAggressive"]?.toBoolean() ?: false,
        onCheckedChange = { onPropertyChange("isAggressive", it.toString()) },
        label = "Hostile (attacks player)"
    )

    PropertyDropdown(
        value = element.properties["behavior"] ?: "idle",
        onValueChange = { onPropertyChange("behavior", it) },
        label = "Behavior",
        options = listOf("idle", "wander", "follow", "flee", "attack")
    )
}

@Composable
private fun RecipePropertiesEditor(
    element: ModElement,
    onPropertyChange: (String, String) -> Unit
) {
    PropertyTextField(
        value = element.properties["resultItem"] ?: "",
        onValueChange = { onPropertyChange("resultItem", it) },
        label = "Result Item ID"
    )

    PropertyNumberField(
        value = element.properties["resultAmount"] ?: "1",
        onValueChange = { onPropertyChange("resultAmount", it) },
        label = "Result Amount"
    )

    PropertyTextField(
        value = element.properties["ingredients"] ?: "",
        onValueChange = { onPropertyChange("ingredients", it) },
        label = "Ingredients (comma-separated IDs)"
    )

    PropertyDropdown(
        value = element.properties["recipeType"] ?: "shaped",
        onValueChange = { onPropertyChange("recipeType", it) },
        label = "Recipe Type",
        options = listOf("shaped", "shapeless", "smelting")
    )
}

@Composable
private fun PropertyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun PropertyNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PropertyDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.replaceFirstChar { it.uppercase() }) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PropertySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

private fun getElementEmoji(type: ModElementType): String {
    return when (type) {
        ModElementType.ITEM -> "⭐"
        ModElementType.BLOCK -> "🧱"
        ModElementType.TOOL -> "⛏️"
        ModElementType.ARMOR -> "🛡️"
        ModElementType.MOB -> "🐱"
        ModElementType.RECIPE -> "🍖"
    }
}
