package com.minecraftmodcreator.ui.screens.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.minecraftmodcreator.domain.model.Mod
import com.minecraftmodcreator.domain.model.ModElement
import com.minecraftmodcreator.domain.model.Template
import com.minecraftmodcreator.domain.model.TemplateCategory
import com.minecraftmodcreator.domain.repository.ModRepository
import com.minecraftmodcreator.ui.theme.*
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatesScreen(
    onTemplateClick: (String) -> Unit,
    viewModel: TemplatesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showUseTemplateDialog by remember { mutableStateOf<Template?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Templates") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category Filter
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedCategory == null,
                        onClick = { viewModel.filterByCategory(null) },
                        label = { Text("All") },
                        leadingIcon = if (uiState.selectedCategory == null) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
                items(TemplateCategory.entries) { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { viewModel.filterByCategory(category) },
                        label = { Text(getCategoryName(category)) },
                        leadingIcon = if (uiState.selectedCategory == category) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }

            val filteredTemplates = if (uiState.selectedCategory == null) {
                uiState.templates
            } else {
                uiState.templates.filter { it.category == uiState.selectedCategory }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTemplates) { template ->
                    TemplateCard(
                        template = template,
                        onUseTemplate = { showUseTemplateDialog = template }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    // Use Template Dialog
    showUseTemplateDialog?.let { template ->
        AlertDialog(
            onDismissRequest = { showUseTemplateDialog = null },
            title = { Text("Use Template") },
            text = {
                Column {
                    Text("Do you want to create a new mod using the \"${template.name}\" template?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This will create a new mod with ${template.elements.size} elements.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showUseTemplateDialog = null
                    onTemplateClick(template.id)
                }) {
                    Text("Create Mod")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUseTemplateDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun TemplateCard(
    template: Template,
    onUseTemplate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(getCategoryColor(template.category).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getCategoryEmoji(template.category),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = template.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = template.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AssistChip(
                        onClick = {},
                        label = { Text(getCategoryName(template.category)) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = getCategoryColor(template.category).copy(alpha = 0.1f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Preview elements
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                template.elements.take(4).forEach { element ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(getElementTypeColor(element.type).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getElementEmoji(element.type),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                if (template.elements.size > 4) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${template.elements.size - 4}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                FilledTonalButton(onClick = onUseTemplate) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Use")
                }
            }
        }
    }
}

private fun getCategoryName(category: TemplateCategory): String {
    return when (category) {
        TemplateCategory.DECORATION -> "Decoration"
        TemplateCategory.UTILITY -> "Utility"
        TemplateCategory.ADVENTURE -> "Adventure"
        TemplateCategory.CHALLENGE -> "Challenge"
    }
}

private fun getCategoryColor(category: TemplateCategory): Color {
    return when (category) {
        TemplateCategory.DECORATION -> Color(0xFFE91E63)
        TemplateCategory.UTILITY -> Color(0xFF2196F3)
        TemplateCategory.ADVENTURE -> Color(0xFF4CAF50)
        TemplateCategory.CHALLENGE -> Color(0xFFFF9800)
    }
}

private fun getCategoryEmoji(category: TemplateCategory): String {
    return when (category) {
        TemplateCategory.DECORATION -> "🎨"
        TemplateCategory.UTILITY -> "🔧"
        TemplateCategory.ADVENTURE -> "⚔️"
        TemplateCategory.CHALLENGE -> "🏆"
    }
}

private fun getElementTypeColor(type: com.minecraftmodcreator.domain.model.ModElementType): Color {
    return when (type) {
        com.minecraftmodcreator.domain.model.ModElementType.ITEM -> ItemColor
        com.minecraftmodcreator.domain.model.ModElementType.BLOCK -> BlockColor
        com.minecraftmodcreator.domain.model.ModElementType.TOOL -> ToolColor
        com.minecraftmodcreator.domain.model.ModElementType.ARMOR -> ArmorColor
        com.minecraftmodcreator.domain.model.ModElementType.MOB -> MobColor
        com.minecraftmodcreator.domain.model.ModElementType.RECIPE -> RecipeColor
        else -> Color(0xFF9E9E9E)
    }
}

private fun getElementEmoji(type: com.minecraftmodcreator.domain.model.ModElementType): String {
    return when (type) {
        com.minecraftmodcreator.domain.model.ModElementType.ITEM -> "⭐"
        com.minecraftmodcreator.domain.model.ModElementType.BLOCK -> "🧱"
        com.minecraftmodcreator.domain.model.ModElementType.TOOL -> "⛏️"
        com.minecraftmodcreator.domain.model.ModElementType.ARMOR -> "🛡️"
        com.minecraftmodcreator.domain.model.ModElementType.MOB -> "🐱"
        com.minecraftmodcreator.domain.model.ModElementType.RECIPE -> "🍖"
        else -> type.emoji
    }
}
