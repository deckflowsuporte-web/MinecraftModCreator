package com.minecraftmodcreator.ui.screens.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class OreConfig(
    val name: String,
    val blockId: String,
    val color: Color,
    var visible: Boolean = true,
    var opacity: Float = 1f
)

data class XRaySettings(
    var enabled: Boolean = true,
    var range: Int = 16,
    var depth: Int = 64,
    var wireframeEnabled: Boolean = false,
    var showOresOnly: Boolean = false,
    var ores: List<OreConfig> = defaultOres
)

val defaultOres = listOf(
    OreConfig("Diamond Ore", "minecraft:diamond_ore", Color(0xFF4FC3F7)),
    OreConfig("Emerald Ore", "minecraft:emerald_ore", Color(0xFF50FA7B)),
    OreConfig("Gold Ore", "minecraft:gold_ore", Color(0xFFFFD700)),
    OreConfig("Iron Ore", "minecraft:iron_ore", Color(0xFFBDB76B)),
    OreConfig("Coal Ore", "minecraft:coal_ore", Color(0xFF333333)),
    OreConfig("Copper Ore", "minecraft:copper_ore", Color(0xFFE87C5D)),
    OreConfig("Lapis Ore", "minecraft:lapis_ore", Color(0xFF3F51B5)),
    OreConfig("Redstone Ore", "minecraft:redstone_ore", Color(0xFFB71C1C)),
    OreConfig("Ancient Debris", "minecraft:ancient_debris", Color(0xFF8B4513)),
    OreConfig("Nether Gold Ore", "minecraft:nether_gold_ore", Color(0xFFFFCA28)),
    OreConfig("Nether Quartz Ore", "minecraft:nether_quartz_ore", Color(0xFFFFFDF5)),
    OreConfig("Debris", "minecraft:debris", Color(0xFF5D4037))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XRayEditorScreen(
    initialSettings: XRaySettings = XRaySettings(),
    onBack: () -> Unit,
    onSave: (XRaySettings) -> Unit
) {
    var settings by remember { mutableStateOf(initialSettings) }
    var selectedTab by remember { mutableIntStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("X-Ray Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onSave(settings) }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("General") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Ores") },
                    icon = { Icon(Icons.Default.Diamond, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Advanced") },
                    icon = { Icon(Icons.Default.Tune, contentDescription = null) }
                )
            }
            
            when (selectedTab) {
                0 -> GeneralSettingsTab(settings, { settings = it })
                1 -> OresSettingsTab(settings, { settings = it })
                2 -> AdvancedSettingsTab(settings, { settings = it })
            }
        }
    }
}

@Composable
private fun GeneralSettingsTab(
    settings: XRaySettings,
    onSettingsChange: (XRaySettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "General Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Enable/Disable
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Enable X-Ray")
                        }
                        Switch(
                            checked = settings.enabled,
                            onCheckedChange = { 
                                onSettingsChange(settings.copy(enabled = it)) 
                            }
                        )
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    // Range Slider
                    Text(
                        text = "Detection Range: ${settings.range} blocks",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value = settings.range.toFloat(),
                        onValueChange = { 
                            onSettingsChange(settings.copy(range = it.toInt())) 
                        },
                        valueRange = 1f..64f,
                        steps = 62,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    // Depth Slider
                    Text(
                        text = "Max Depth: ${settings.depth} blocks",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value = settings.depth.toFloat(),
                        onValueChange = { 
                            onSettingsChange(settings.copy(depth = it.toInt())) 
                        },
                        valueRange = 1f..128f,
                        steps = 126,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Quick Presets",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = { 
                                onSettingsChange(settings.copy(range = 16, depth = 32))
                            },
                            label = { Text("Near") },
                            leadingIcon = { Icon(Icons.Default.NearMe, contentDescription = null) },
                            modifier = Modifier.weight(1f)
                        )
                        AssistChip(
                            onClick = { 
                                onSettingsChange(settings.copy(range = 32, depth = 64))
                            },
                            label = { Text("Medium") },
                            leadingIcon = { Icon(Icons.Default.Adjust, contentDescription = null) },
                            modifier = Modifier.weight(1f)
                        )
                        AssistChip(
                            onClick = { 
                                onSettingsChange(settings.copy(range = 64, depth = 128))
                            },
                            label = { Text("Far") },
                            leadingIcon = { Icon(Icons.Default.Deblur, contentDescription = null) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OresSettingsTab(
    settings: XRaySettings,
    onSettingsChange: (XRaySettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Block Visibility",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = {
                    onSettingsChange(settings.copy(
                        ores = settings.ores.map { it.copy(visible = true) }
                    ))
                }) {
                    Text("Enable All")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        items(settings.ores) { ore ->
            OreItem(
                ore = ore,
                onOreChange = { updatedOre ->
                    onSettingsChange(settings.copy(
                        ores = settings.ores.map { 
                            if (it.blockId == updatedOre.blockId) updatedOre else it 
                        }
                    ))
                }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    // Add custom ore
                    val newOre = OreConfig(
                        name = "Custom Ore",
                        blockId = "mod:custom_ore",
                        color = Color.Gray
                    )
                    onSettingsChange(settings.copy(ores = settings.ores + newOre))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Custom Block")
            }
        }
    }
}

@Composable
private fun OreItem(
    ore: OreConfig,
    onOreChange: (OreConfig) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ore.color)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = ore.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = ore.blockId,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Switch(
                    checked = ore.visible,
                    onCheckedChange = { 
                        onOreChange(ore.copy(visible = it)) 
                    }
                )
                if (ore.visible) {
                    Text(
                        text = "Opacity: ${(ore.opacity * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
        
        if (ore.visible) {
            Slider(
                value = ore.opacity,
                onValueChange = { 
                    onOreChange(ore.copy(opacity = it)) 
                },
                valueRange = 0.1f..1f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun AdvancedSettingsTab(
    settings: XRaySettings,
    onSettingsChange: (XRaySettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Visual Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Wireframe
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.GridOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Wireframe Mode")
                                Text(
                                    text = "Show block outlines",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Switch(
                            checked = settings.wireframeEnabled,
                            onCheckedChange = { 
                                onSettingsChange(settings.copy(wireframeEnabled = it)) 
                            }
                        )
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    // Show Ores Only
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.FilterCenterFocus,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Show Ores Only")
                                Text(
                                    text = "Hide all non-ore blocks",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Switch(
                            checked = settings.showOresOnly,
                            onCheckedChange = { 
                                onSettingsChange(settings.copy(showOresOnly = it)) 
                            }
                        )
                    }
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Performance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Range: ${settings.range} blocks | Depth: ${settings.depth} blocks",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val estimatedBlocks = settings.range * settings.range * settings.depth
                    val performanceLevel = when {
                        estimatedBlocks < 10000 -> "Low"
                        estimatedBlocks < 50000 -> "Medium"
                        else -> "High"
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            when (performanceLevel) {
                                "Low" -> Icons.Default.BatteryFull
                                "Medium" -> Icons.Default.Battery5Bar
                                else -> Icons.Default.BatteryAlert
                            },
                            contentDescription = null,
                            tint = when (performanceLevel) {
                                "Low" -> Color(0xFF4CAF50)
                                "Medium" -> Color(0xFFFF9800)
                                else -> Color(0xFFF44336)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Performance Impact: $performanceLevel",
                            style = MaterialTheme.typography.bodyMedium,
                            color = when (performanceLevel) {
                                "Low" -> Color(0xFF4CAF50)
                                "Medium" -> Color(0xFFFF9800)
                                else -> Color(0xFFF44336)
                            }
                        )
                    }
                }
            }
        }
    }
}
