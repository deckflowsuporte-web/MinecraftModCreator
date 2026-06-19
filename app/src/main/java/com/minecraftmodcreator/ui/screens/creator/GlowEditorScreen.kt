package com.minecraftmodcreator.ui.screens.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class GlowTarget {
    MOBS, BLOCKS, ITEMS, ALL
}

enum class GlowColorType {
    FIXED, RAINBOW, PULSING, BREATHING
}

data class GlowConfig(
    val id: String = System.currentTimeMillis().toString(),
    val name: String = "",
    val target: GlowTarget = GlowTarget.ALL,
    val targetId: String = "", // Optional specific target
    val colorType: GlowColorType = GlowColorType.FIXED,
    val fixedColor: Color = Color(0xFFFFFF00),
    val rainbowSpeed: Float = 1f,
    val pulseSpeed: Float = 1f,
    val pulseMinAlpha: Float = 0.3f,
    val breathingSpeed: Float = 1f,
    val intensity: Float = 1f,
    val enabled: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlowEditorScreen(
    initialGlows: List<GlowConfig> = emptyList(),
    onBack: () -> Unit,
    onSave: (List<GlowConfig>) -> Unit
) {
    var glows by remember { mutableStateOf(initialGlows) }
    var selectedGlow by remember { mutableStateOf<GlowConfig?>(null) }
    var showNewGlowDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Glow Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showNewGlowDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Glow")
                    }
                    IconButton(onClick = { onSave(glows) }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewGlowDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Glow Effect")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (glows.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.LightMode,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No glow effects yet",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Tap + to add a new glow effect",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(glows) { glow ->
                        GlowEffectCard(
                            glow = glow,
                            onClick = { selectedGlow = glow },
                            onToggle = {
                                glows = glows.map { 
                                    if (it.id == glow.id) it.copy(enabled = !it.enabled) else it 
                                }
                            },
                            onDelete = {
                                glows = glows.filter { it.id != glow.id }
                            }
                        )
                    }
                }
            }
        }
        
        // Edit Glow Bottom Sheet
        if (selectedGlow != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedGlow = null }
            ) {
                GlowEditorSheet(
                    glow = selectedGlow!!,
                    onGlowChange = { updatedGlow ->
                        glows = glows.map { 
                            if (it.id == updatedGlow.id) updatedGlow else it 
                        }
                        selectedGlow = updatedGlow
                    },
                    onDismiss = { selectedGlow = null }
                )
            }
        }
        
        // New Glow Dialog
        if (showNewGlowDialog) {
            NewGlowDialog(
                onDismiss = { showNewGlowDialog = false },
                onConfirm = { name, target ->
                    val newGlow = GlowConfig(
                        name = name,
                        target = target
                    )
                    glows = glows + newGlow
                    showNewGlowDialog = false
                    selectedGlow = newGlow
                }
            )
        }
    }
}

@Composable
private fun GlowEffectCard(
    glow: GlowConfig,
    onClick: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val gradientColors = when (glow.colorType) {
        GlowColorType.RAINBOW -> listOf(
            Color.Red, Color.Green, Color.Blue
        )
        GlowColorType.FIXED -> listOf(
            glow.fixedColor,
            glow.fixedColor.copy(alpha = 0.5f)
        )
        else -> listOf(
            glow.fixedColor,
            glow.fixedColor.copy(alpha = 0.7f)
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (glow.enabled) 4.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Glow Preview
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(gradientColors)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (glow.target) {
                        GlowTarget.MOBS -> Icons.Default.Pets
                        GlowTarget.BLOCKS -> Icons.Default.ViewModule
                        GlowTarget.ITEMS -> Icons.Default.Inventory2
                        GlowTarget.ALL -> Icons.Default.LightMode
                    },
                    contentDescription = null,
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = glow.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${glow.target.name} • ${glow.colorType.name.lowercase()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            // Toggle
            Switch(
                checked = glow.enabled,
                onCheckedChange = { onToggle() }
            )
            
            // Delete
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GlowEditorSheet(
    glow: GlowConfig,
    onGlowChange: (GlowConfig) -> Unit,
    onDismiss: () -> Unit
) {
    val colorOptions = listOf(
        Color.Yellow, Color.Red, Color.Green, Color.Blue, Color.Magenta,
        Color.Cyan, Color(0xFFFF6B00), Color(0xFFFF69B4), Color(0xFF00FF00),
        Color.White, Color(0xFFFF1493), Color(0xFF9400D3)
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Edit Glow Effect",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Name
        OutlinedTextField(
            value = glow.name,
            onValueChange = { onGlowChange(glow.copy(name = it)) },
            label = { Text("Effect Name") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Target Selection
        Text(
            text = "Target",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(GlowTarget.entries) { target ->
                FilterChip(
                    selected = glow.target == target,
                    onClick = { onGlowChange(glow.copy(target = target)) },
                    label = { Text(target.name.lowercase().replaceFirstChar { it.uppercase() }) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Color Type
        Text(
            text = "Color Type",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(GlowColorType.entries) { colorType ->
                FilterChip(
                    selected = glow.colorType == colorType,
                    onClick = { onGlowChange(glow.copy(colorType = colorType)) },
                    label = { 
                        Text(
                            when (colorType) {
                                GlowColorType.FIXED -> "Fixed"
                                GlowColorType.RAINBOW -> "Rainbow"
                                GlowColorType.PULSING -> "Pulsing"
                                GlowColorType.BREATHING -> "Breathing"
                            }
                        )
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Color Selection (for fixed color)
        if (glow.colorType == GlowColorType.FIXED) {
            Text(
                text = "Color",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(colorOptions) { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (glow.fixedColor == color) {
                                    Modifier.border(3.dp, Color.White, CircleShape)
                                } else Modifier
                            )
                            .clickable { onGlowChange(glow.copy(fixedColor = color)) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Intensity Slider
        Text(
            text = "Intensity: ${(glow.intensity * 100).toInt()}%",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )
        Slider(
            value = glow.intensity,
            onValueChange = { onGlowChange(glow.copy(intensity = it)) },
            valueRange = 0.1f..2f,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun NewGlowDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, GlowTarget) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedTarget by remember { mutableStateOf(GlowTarget.ALL) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Glow Effect") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Effect Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Target",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    GlowTarget.entries.forEach { target ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTarget = target }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTarget == target,
                                onClick = { selectedTarget = target }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = target.name.lowercase().replaceFirstChar { it.uppercase() }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name.ifBlank { "Glow Effect" }, selectedTarget) },
                enabled = name.isNotBlank() || true
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
