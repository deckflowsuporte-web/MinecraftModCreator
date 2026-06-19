package com.minecraftmodcreator.ui.screens.creator

import androidx.compose.foundation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============== TRIGGER TYPES ==============
enum class TriggerType(val displayName: String, val emoji: String, val description: String) {
    // Player Actions
    PLAYER_SNEAK("Sneak", "🧍", "When player crouches/sneaks"),
    PLAYER_SPRINT("Sprint", "🏃", "When player sprints"),
    PLAYER_JUMP("Jump", "⬆️", "When player jumps"),
    PLAYER_HURT("Take Damage", "💔", "When player takes damage"),
    PLAYER_DEATH("Death", "💀", "When player dies"),
    PLAYER_RESPAWN("Respawn", "✨", "When player respawns"),
    PLAYER_LEVEL_UP("Level Up", "⬆️", "When player gains XP level"),
    
    // Item Actions
    ITEM_USE("Use Item", "👆", "When item is used/right-clicked"),
    ITEM_HOLD("Hold Item", "✋", "When item is held in hand"),
    ITEM_SWAP("Swap Item", "🔄", "When item is swapped in hotbar"),
    ITEM_DROP("Drop Item", "📦", "When item is dropped"),
    ITEM_BREAK("Item Break", "💥", "When item durability breaks"),
    
    // Block Actions
    BLOCK_BREAK("Break Block", "⛏️", "When a block is broken"),
    BLOCK_PLACE("Place Block", "🧱", "When a block is placed"),
    BLOCK_INTERACT("Interact", "👆", "When player interacts with block"),
    BLOCK_STEP("Step On", "👣", "When player steps on block"),
    
    // Entity Actions
    ENTITY_KILL("Kill Entity", "💀", "When entity is killed"),
    ENTITY_SPAWN("Entity Spawn", "🐱", "When entity spawns"),
    ENTITY_DAMAGE("Damage Entity", "⚔️", "When entity takes damage"),
    ENTITY_INTERACT("Entity Interact", "🤝", "When player interacts with entity"),
    
    // World Actions
    WORLD_TIME("Time Pass", "⏰", "Every X seconds of game time"),
    WORLD_ENTER("Enter World", "🌍", "When entering the world"),
    WORLD_CHAT("Chat Message", "💬", "When player sends chat message"),
    WORLD_COMMAND("Command", "💻", "When command is executed"),
    
    // Combat
    PLAYER_ATTACK("Attack", "⚔️", "When player attacks"),
    PLAYER_BLOCK("Block", "🛡️", "When player blocks with shield"),
    
    // Inventory
    INVENTORY_OPEN("Open Inventory", "🎒", "When inventory is opened"),
    INVENTORY_CLOSE("Close Inventory", "❌", "When inventory is closed"),
    CRAFT_ITEM("Craft", "🔨", "When item is crafted")
}

// ============== ACTION TYPES ==============
enum class ActionType(val displayName: String, val emoji: String, val description: String) {
    // Item Actions
    GIVE_ITEM("Give Item", "🎁", "Give item to player"),
    REMOVE_ITEM("Remove Item", "🗑️", "Remove item from player"),
    MULTIPLY_ITEM("Multiply Item", "✖️", "Duplicate held item"),
    SET_ITEM("Set Item", "📦", "Replace held item"),
    
    // Entity Actions
    SPAWN_ENTITY("Spawn Entity", "🐱", "Spawn a mob/entity"),
    KILL_ENTITY("Kill Entity", "💀", "Kill target entity"),
    TELEPORT_PLAYER("Teleport", "🌀", "Teleport player"),
    TELEPORT_ENTITY("Teleport Entity", "🌀", "Teleport entity"),
    
    // Effects
    APPLY_EFFECT("Apply Effect", "✨", "Apply potion effect"),
    REMOVE_EFFECT("Remove Effect", "❌", "Remove potion effect"),
    HEAL("Heal", "❤️", "Heal player or entity"),
    DAMAGE("Damage", "💔", "Deal damage to entity"),
    
    // World Actions
    SET_BLOCK("Set Block", "🧱", "Place a block"),
    REMOVE_BLOCK("Remove Block", "💥", "Remove a block"),
    EXECUTE_COMMAND("Run Command", "💻", "Execute game command"),
    BROADCAST_MESSAGE("Send Message", "💬", "Send chat message"),
    
    // Player Actions
    SET_HEALTH("Set Health", "❤️", "Set player health"),
    SET_FOOD("Set Food", "🍖", "Set food level"),
    SET_GAMEMODE("Set Gamemode", "🎮", "Change gamemode"),
    SET_EXP("Give XP", "⬆️", "Give experience"),
    
    // Sound & Visual
    PLAY_SOUND("Play Sound", "🔊", "Play a sound effect"),
    SPAWN_PARTICLE("Particles", "✨", "Spawn particle effect"),
    
    // Logic
    DELAY("Wait", "⏱️", "Wait before next action"),
    LOOP("Repeat", "🔄", "Repeat actions X times"),
    CONDITION("If/Else", "⚖️", "Conditional logic")
}

// ============== CONDITION TYPES ==============
enum class ConditionType(val displayName: String, val emoji: String) {
    HAS_ITEM("Has Item", "🎒"),
    HAS_XP("Has XP Level", "⬆️"),
    HAS_EFFECT("Has Effect", "✨"),
    HEALTH_ABOVE("Health Above", "❤️"),
    HEALTH_BELOW("Health Below", "💔"),
    IN_BIOME("In Biome", "🌲"),
    TIME_OF_DAY("Time of Day", "⏰"),
    IS_SNEAKING("Is Sneaking", "🧍"),
    IS_SPRINTING("Is Sprinting", "🏃"),
    INVENTORY_FULL("Inventory Full", "📦"),
    BLOCK_ABOVE("Block Above", "🧱"),
    TOOL_BROKEN("Tool Broken", "💥"),
    PLAYER_HURT("Player Hurt", "💔"),
    RANDOM_CHANCE("Random Chance", "🎲")
}

// ============== MOD ACTION ==============
data class ModAction(
    val id: String = java.util.UUID.randomUUID().toString(),
    val trigger: TriggerType,
    val conditions: List<ActionCondition> = emptyList(),
    val actions: List<ActionStep> = emptyList(),
    val enabled: Boolean = true,
    val name: String = ""
)

data class ActionCondition(
    val id: String = java.util.UUID.randomUUID().toString(),
    val conditionType: ConditionType,
    val value: String = "",
    val negate: Boolean = false // true = "NOT"
)

data class ActionStep(
    val id: String = java.util.UUID.randomUUID().toString(),
    val actionType: ActionType,
    val target: String = "@s", // @s, @p, @e, etc.
    val params: Map<String, String> = emptyMap()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionEditorScreen(
    initialActions: List<ModAction> = emptyList(),
    onBack: () -> Unit,
    onSave: (List<ModAction>) -> Unit
) {
    var actions by remember { mutableStateOf(initialActions) }
    var selectedAction by remember { mutableStateOf<ModAction?>(null) }
    var showNewDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Action Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showNewDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Action")
                    }
                    IconButton(onClick = { onSave(actions) }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Action")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (actions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.AutoFixHigh,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No actions yet",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Create actions to modify game behavior",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showNewDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create Action")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(actions) { action ->
                        ActionCard(
                            action = action,
                            onClick = { selectedAction = action },
                            onToggle = {
                                actions = actions.map { 
                                    if (it.id == action.id) it.copy(enabled = !it.enabled) else it 
                                }
                            },
                            onDelete = {
                                actions = actions.filter { it.id != action.id }
                            }
                        )
                    }
                }
            }
        }
        
        // Action Editor Bottom Sheet
        if (selectedAction != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedAction = null }
            ) {
                ActionEditorSheet(
                    action = selectedAction!!,
                    onActionChange = { updated ->
                        actions = actions.map { 
                            if (it.id == updated.id) updated else it 
                        }
                        selectedAction = updated
                    },
                    onDismiss = { selectedAction = null }
                )
            }
        }
        
        // New Action Dialog
        if (showNewDialog) {
            NewActionDialog(
                onDismiss = { showNewDialog = false },
                onConfirm = { name, trigger ->
                    val newAction = ModAction(
                        name = name,
                        trigger = trigger
                    )
                    actions = actions + newAction
                    showNewDialog = false
                    selectedAction = newAction
                }
            )
        }
    }
}

@Composable
private fun ActionCard(
    action: ModAction,
    onClick: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (action.enabled) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = action.trigger.emoji,
                            fontSize = 24.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = action.name.ifEmpty { action.trigger.displayName },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "WHEN: ${action.trigger.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Row {
                    Switch(
                        checked = action.enabled,
                        onCheckedChange = { onToggle() }
                    )
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Preview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "→ ${action.actions.size} action(s)${if (action.conditions.isNotEmpty()) " | ${action.conditions.size} condition(s)" else ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ActionEditorSheet(
    action: ModAction,
    onActionChange: (ModAction) -> Unit,
    onDismiss: () -> Unit
) {
    var activeTab by remember { mutableIntStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Edit Action",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = action.name,
            onValueChange = { onActionChange(action.copy(name = it)) },
            label = { Text("Action Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tabs
        TabRow(selectedTabIndex = activeTab) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = { Text("Trigger") },
                icon = { Icon(Icons.Default.FlashOn, contentDescription = null) }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = { Text("Conditions (${action.conditions.size})") },
                icon = { Icon(Icons.Default.Balance, contentDescription = null) }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = { Text("Actions (${action.actions.size})") },
                icon = { Icon(Icons.Default.Build, contentDescription = null) }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (activeTab) {
            0 -> TriggerTab(action, onActionChange)
            1 -> ConditionsTab(action, onActionChange)
            2 -> ActionsTab(action, onActionChange)
        }
        
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
private fun TriggerTab(action: ModAction, onActionChange: (ModAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "This action triggers when:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Group triggers by category
        val playerTriggers = TriggerType.entries.filter { 
            it.name.startsWith("PLAYER") || it.name.startsWith("ITEM") || it.name.startsWith("INVENTORY")
        }
        val worldTriggers = TriggerType.entries.filter { 
            it.name.startsWith("BLOCK") || it.name.startsWith("ENTITY") || it.name.startsWith("WORLD") 
        }
        val combatTriggers = TriggerType.entries.filter { 
            it.name.contains("ATTACK") || it.name.contains("BLOCK") || it.name.contains("CRAFT")
        }
        
        Text(
            text = "👤 Player & Items",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        playerTriggers.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { trigger ->
                    FilterChip(
                        selected = action.trigger == trigger,
                        onClick = { onActionChange(action.copy(trigger = trigger)) },
                        label = { Text(trigger.emoji + " " + trigger.displayName) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "🌍 World & Blocks",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        worldTriggers.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { trigger ->
                    FilterChip(
                        selected = action.trigger == trigger,
                        onClick = { onActionChange(action.copy(trigger = trigger)) },
                        label = { Text(trigger.emoji + " " + trigger.displayName) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "⚔️ Combat & Crafting",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        combatTriggers.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { trigger ->
                    FilterChip(
                        selected = action.trigger == trigger,
                        onClick = { onActionChange(action.copy(trigger = trigger)) },
                        label = { Text(trigger.emoji + " " + trigger.displayName) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun ConditionsTab(action: ModAction, onActionChange: (ModAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Conditions (optional)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = {
                onActionChange(action.copy(
                    conditions = action.conditions + ActionCondition(conditionType = ConditionType.HAS_ITEM)
                ))
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add")
            }
        }
        
        Text(
            text = "All conditions must be true for actions to execute",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            action.conditions.forEachIndexed { index, condition ->
                ConditionItem(
                    condition = condition,
                    onChange = { updated ->
                        onActionChange(action.copy(
                            conditions = action.conditions.toMutableList().apply {
                                this[index] = updated
                            }
                        ))
                    },
                    onDelete = {
                        onActionChange(action.copy(
                            conditions = action.conditions.filter { it.id != condition.id }
                        ))
                    }
                )
            }
            
            if (action.conditions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("No conditions - action will always execute")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConditionItem(
    condition: ActionCondition,
    onChange: (ActionCondition) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = !condition.negate,
                        onCheckedChange = { onChange(condition.copy(negate = !it)) }
                    )
                    Text(
                        text = if (condition.negate) "NOT" else "IF",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (condition.negate) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Close, contentDescription = "Delete")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Condition Type Selector
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = condition.conditionType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Condition") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ConditionType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.emoji + " " + type.displayName) },
                            onClick = {
                                onChange(condition.copy(conditionType = type))
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Value input
            OutlinedTextField(
                value = condition.value,
                onValueChange = { onChange(condition.copy(value = it)) },
                label = { Text("Value") },
                placeholder = { Text(getConditionPlaceholder(condition.conditionType)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

private fun getConditionPlaceholder(condition: ConditionType): String {
    return when (condition) {
        ConditionType.HAS_ITEM -> "e.g., diamond 1"
        ConditionType.HAS_XP -> "e.g., 10"
        ConditionType.HAS_EFFECT -> "e.g., speed"
        ConditionType.HEALTH_ABOVE, ConditionType.HEALTH_BELOW -> "e.g., 10"
        ConditionType.IN_BIOME -> "e.g., plains"
        ConditionType.TIME_OF_DAY -> "e.g., night"
        ConditionType.BLOCK_ABOVE -> "e.g., stone"
        ConditionType.RANDOM_CHANCE -> "e.g., 50 (percent)"
        else -> ""
    }
}

@Composable
private fun ActionsTab(action: ModAction, onActionChange: (ModAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Actions to execute",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = {
                onActionChange(action.copy(
                    actions = action.actions + ActionStep(actionType = ActionType.GIVE_ITEM)
                ))
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add")
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            action.actions.forEachIndexed { index, step ->
                ActionStepItem(
                    step = step,
                    index = index,
                    onChange = { updated ->
                        onActionChange(action.copy(
                            actions = action.actions.toMutableList().apply {
                                this[index] = updated
                            }
                        ))
                    },
                    onDelete = {
                        onActionChange(action.copy(
                            actions = action.actions.filter { it.id != step.id }
                        ))
                    }
                )
            }
            
            if (action.actions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Add at least one action!")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionStepItem(
    step: ActionStep,
    index: Int,
    onChange: (ActionStep) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Step ${index + 1}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Close, contentDescription = "Delete")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Action Type Selector
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = step.actionType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Action") },
                    leadingIcon = { Text(step.actionType.emoji) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ActionType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.emoji + " " + type.displayName) },
                            onClick = {
                                onChange(step.copy(actionType = type))
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Target
            OutlinedTextField(
                value = step.target,
                onValueChange = { onChange(step.copy(target = it)) },
                label = { Text("Target") },
                placeholder = { Text("@s (self), @p (nearest), @e (all)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Parameters based on action type
            Spacer(modifier = Modifier.height(8.dp))
            ActionParameters(step, onChange)
        }
    }
}

@Composable
private fun ActionParameters(step: ActionStep, onChange: (ActionStep) -> Unit) {
    when (step.actionType) {
        ActionType.GIVE_ITEM, ActionType.SET_ITEM -> {
            OutlinedTextField(
                value = step.params["item"] ?: "",
                onValueChange = { onChange(step.copy(params = step.params + ("item" to it))) },
                label = { Text("Item ID") },
                placeholder = { Text("e.g., diamond, iron_sword") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = step.params["count"] ?: "1",
                onValueChange = { onChange(step.copy(params = step.params + ("count" to it))) },
                label = { Text("Count") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        ActionType.MULTIPLY_ITEM -> {
            OutlinedTextField(
                value = step.params["multiplier"] ?: "2",
                onValueChange = { onChange(step.copy(params = step.params + ("multiplier" to it))) },
                label = { Text("Multiplier") },
                placeholder = { Text("e.g., 2 (double), 3 (triple)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        ActionType.SPAWN_ENTITY -> {
            OutlinedTextField(
                value = step.params["entity"] ?: "",
                onValueChange = { onChange(step.copy(params = step.params + ("entity" to it))) },
                label = { Text("Entity ID") },
                placeholder = { Text("e.g., cow, zombie, wolf") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        ActionType.TELEPORT_PLAYER, ActionType.TELEPORT_ENTITY -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = step.params["x"] ?: "~",
                    onValueChange = { onChange(step.copy(params = step.params + ("x" to it))) },
                    label = { Text("X") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = step.params["y"] ?: "~",
                    onValueChange = { onChange(step.copy(params = step.params + ("y" to it))) },
                    label = { Text("Y") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = step.params["z"] ?: "~",
                    onValueChange = { onChange(step.copy(params = step.params + ("z" to it))) },
                    label = { Text("Z") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }
        ActionType.APPLY_EFFECT -> {
            OutlinedTextField(
                value = step.params["effect"] ?: "",
                onValueChange = { onChange(step.copy(params = step.params + ("effect" to it))) },
                label = { Text("Effect") },
                placeholder = { Text("e.g., speed, strength, jump_boost") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = step.params["duration"] ?: "30",
                onValueChange = { onChange(step.copy(params = step.params + ("duration" to it))) },
                label = { Text("Duration (seconds)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        ActionType.EXECUTE_COMMAND -> {
            OutlinedTextField(
                value = step.params["command"] ?: "",
                onValueChange = { onChange(step.copy(params = step.params + ("command" to it))) },
                label = { Text("Command") },
                placeholder = { Text("e.g., /give @s diamond 1") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        ActionType.BROADCAST_MESSAGE -> {
            OutlinedTextField(
                value = step.params["message"] ?: "",
                onValueChange = { onChange(step.copy(params = step.params + ("message" to it))) },
                label = { Text("Message") },
                placeholder = { Text("Message to send") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        ActionType.PLAY_SOUND -> {
            OutlinedTextField(
                value = step.params["sound"] ?: "",
                onValueChange = { onChange(step.copy(params = step.params + ("sound" to it))) },
                label = { Text("Sound ID") },
                placeholder = { Text("e.g., entity.player.levelup") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        ActionType.DELAY -> {
            OutlinedTextField(
                value = step.params["ticks"] ?: "20",
                onValueChange = { onChange(step.copy(params = step.params + ("ticks" to it))) },
                label = { Text("Delay (ticks)") },
                placeholder = { Text("20 ticks = 1 second") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        else -> {
            OutlinedTextField(
                value = step.params["value"] ?: "",
                onValueChange = { onChange(step.copy(params = step.params + ("value" to it))) },
                label = { Text("Value") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
private fun NewActionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, TriggerType) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedTrigger by remember { mutableStateOf(TriggerType.PLAYER_SNEAK) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Action") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Action Name") },
                    placeholder = { Text("e.g., Sneak to Duplicate") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Trigger Event",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(TriggerType.entries) { trigger ->
                        ListItem(
                            headlineContent = { Text(trigger.emoji + " " + trigger.displayName) },
                            supportingContent = { Text(trigger.description) },
                            leadingContent = {
                                RadioButton(
                                    selected = selectedTrigger == trigger,
                                    onClick = { selectedTrigger = trigger }
                                )
                            },
                            modifier = Modifier.clickable { selectedTrigger = trigger }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name.ifEmpty { selectedTrigger.displayName }, selectedTrigger) }) {
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
