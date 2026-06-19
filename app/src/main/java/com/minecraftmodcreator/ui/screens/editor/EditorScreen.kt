package com.minecraftmodcreator.ui.screens.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.minecraftmodcreator.domain.model.Mod
import com.minecraftmodcreator.domain.model.ModElement
import com.minecraftmodcreator.domain.model.ModElementType
import com.minecraftmodcreator.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    modId: String?,
    onNavigateBack: () -> Unit,
    onElementClick: (String, String?) -> Unit,
    onExportClick: (String) -> Unit,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddElementSheet by remember { mutableStateOf(false) }
    var showSaveSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            showSaveSnackbar = true
            viewModel.clearSaveSuccess()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(showSaveSnackbar) {
        if (showSaveSnackbar) {
            snackbarHostState.showSnackbar("Mod saved successfully!")
            showSaveSnackbar = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isNewMod) "New Mod" else "Edit Mod")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!uiState.isNewMod) {
                        IconButton(onClick = { onExportClick(uiState.mod.id) }) {
                            Icon(Icons.Default.Share, contentDescription = "Export")
                        }
                    }
                    IconButton(
                        onClick = { viewModel.saveMod() },
                        enabled = uiState.mod.name.isNotBlank() && !uiState.isSaving
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddElementSheet = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Element")
            }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    ModInfoSection(
                        name = uiState.mod.name,
                        description = uiState.mod.description,
                        version = uiState.mod.version,
                        onNameChange = viewModel::updateModName,
                        onDescriptionChange = viewModel::updateModDescription,
                        onVersionChange = viewModel::updateModVersion
                    )
                }

                item {
                    Text(
                        text = "Elements (${uiState.mod.elements.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (uiState.mod.elements.isEmpty()) {
                    item {
                        EmptyElementsSection()
                    }
                } else {
                    items(uiState.mod.elements, key = { it.id }) { element ->
                        ElementCard(
                            element = element,
                            onClick = { onElementClick(uiState.mod.id, element.id) },
                            onDelete = { viewModel.deleteElement(element.id) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    if (showAddElementSheet) {
        AddElementBottomSheet(
            onDismiss = { showAddElementSheet = false },
            onElementSelected = { elementType ->
                showAddElementSheet = false
                val newElement = ModElement(
                    id = java.util.UUID.randomUUID().toString(),
                    type = elementType,
                    name = "New ${elementType.name.lowercase().replaceFirstChar { it.uppercase() }}"
                )
                viewModel.addElement(newElement)
                if (uiState.isNewMod) {
                    viewModel.saveMod()
                }
                onElementClick(uiState.mod.id, newElement.id)
            }
        )
    }
}

@Composable
private fun ModInfoSection(
    name: String,
    description: String,
    version: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onVersionChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Mod Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            OutlinedTextField(
                value = version,
                onValueChange = onVersionChange,
                label = { Text("Version") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
private fun ElementCard(
    element: ModElement,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getElementColor(element.type).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getElementEmoji(element.type),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = element.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = element.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = getElementColor(element.type)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EmptyElementsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔧",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No elements yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add items, blocks, tools, and more!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddElementBottomSheet(
    onDismiss: () -> Unit,
    onElementSelected: (ModElementType) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Add Element",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            ElementTypeButton(
                icon = Icons.Default.Star,
                emoji = "⭐",
                title = "Item",
                description = "Custom items for your mod",
                color = ItemColor,
                onClick = { onElementSelected(ModElementType.ITEM) }
            )

            ElementTypeButton(
                icon = Icons.Default.GridOn,
                emoji = "🧱",
                title = "Block",
                description = "New blocks to build with",
                color = BlockColor,
                onClick = { onElementSelected(ModElementType.BLOCK) }
            )

            ElementTypeButton(
                icon = Icons.Default.Build,
                emoji = "⛏️",
                title = "Tool",
                description = "Pickaxes, axes, swords, and more",
                color = ToolColor,
                onClick = { onElementSelected(ModElementType.TOOL) }
            )

            ElementTypeButton(
                icon = Icons.Default.Shield,
                emoji = "🛡️",
                title = "Armor",
                description = "Protective gear for players",
                color = ArmorColor,
                onClick = { onElementSelected(ModElementType.ARMOR) }
            )

            ElementTypeButton(
                icon = Icons.Default.Pets,
                emoji = "🐱",
                title = "Mob",
                description = "Friendly or hostile creatures",
                color = MobColor,
                onClick = { onElementSelected(ModElementType.MOB) }
            )

            ElementTypeButton(
                icon = Icons.Default.Restaurant,
                emoji = "🍖",
                title = "Recipe",
                description = "Crafting and smelting recipes",
                color = RecipeColor,
                onClick = { onElementSelected(ModElementType.RECIPE) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ElementTypeButton(
    icon: ImageVector,
    emoji: String,
    title: String,
    description: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = color
            )
        }
    }
}

private fun getElementColor(type: ModElementType): androidx.compose.ui.graphics.Color {
    return when (type) {
        ModElementType.ITEM -> ItemColor
        ModElementType.BLOCK -> BlockColor
        ModElementType.TOOL -> ToolColor
        ModElementType.ARMOR -> ArmorColor
        ModElementType.MOB -> MobColor
        ModElementType.RECIPE -> RecipeColor
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
