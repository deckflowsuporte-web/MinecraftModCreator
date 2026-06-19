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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class UIElementType(val displayName: String) {
    BUTTON("Button"),
    LABEL("Label"),
    IMAGE("Image"),
    SLOT("Slot"),
    TOGGLE("Toggle"),
    RECTANGLE("Rectangle")
}

data class UIElement(
    val id: String = System.currentTimeMillis().toString(),
    val type: UIElementType = UIElementType.BUTTON,
    var text: String = "",
    var backgroundColor: Long = 0xFF4CAF50,
    var textColor: Long = 0xFFFFFFFF,
    var visible: Boolean = true,
    var action: String = ""
)

data class UIScreen(
    val id: String = System.currentTimeMillis().toString(),
    val name: String = "New Screen",
    var elements: List<UIElement> = emptyList(),
    var backgroundColor: Long = 0x80000000
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterfaceEditorScreen(
    initialScreens: List<UIScreen> = emptyList(),
    onBack: () -> Unit,
    onSave: (List<UIScreen>) -> Unit
) {
    var screens by remember { mutableStateOf(initialScreens.ifEmpty { listOf(UIScreen()) }) }
    var selectedScreen by remember { mutableStateOf(screens.firstOrNull()) }
    var showElementDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedScreen?.name ?: "Interface Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        screens = screens + UIScreen(name = "Screen ${screens.size + 1}")
                        selectedScreen = screens.last()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "New Screen")
                    }
                    IconButton(onClick = { onSave(screens) }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showElementDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Element")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Screen Tabs
            ScrollableTabRow(
                selectedTabIndex = screens.indexOf(selectedScreen).coerceAtLeast(0),
                modifier = Modifier.fillMaxWidth()
            ) {
                screens.forEachIndexed { index, screen ->
                    Tab(
                        selected = selectedScreen?.id == screen.id,
                        onClick = { selectedScreen = screen },
                        text = { Text(screen.name) }
                    )
                }
            }
            
            // Screen Preview
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .aspectRatio(0.8f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(selectedScreen?.backgroundColor ?: 0x80000000)
                    )
                ) {
                    if (selectedScreen?.elements.isNullOrEmpty() == true) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Dashboard,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.White.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "No elements",
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedScreen?.elements?.forEach { element ->
                                if (element.visible) {
                                    when (element.type) {
                                        UIElementType.BUTTON -> Button(
                                            onClick = { },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(element.backgroundColor)
                                            )
                                        ) {
                                            Text(
                                                element.text.ifEmpty { "Button" },
                                                color = Color(element.textColor)
                                            )
                                        }
                                        UIElementType.LABEL -> Text(
                                            text = element.text.ifEmpty { "Label" },
                                            color = Color(element.textColor),
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                        UIElementType.SLOT -> Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                                .background(
                                                    Color(element.backgroundColor),
                                                    RoundedCornerShape(4.dp)
                                                )
                                                .border(2.dp, Color.Gray, RoundedCornerShape(4.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Slot", color = Color.White)
                                        }
                                        UIElementType.TOGGLE -> Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Switch(checked = true, onCheckedChange = {})
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(element.text.ifEmpty { "Toggle" })
                                        }
                                        UIElementType.RECTANGLE -> Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp)
                                                .background(
                                                    Color(element.backgroundColor),
                                                    RoundedCornerShape(4.dp)
                                                )
                                        )
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Elements List
            if (!selectedScreen?.elements.isNullOrEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Elements (${selectedScreen?.elements?.size ?: 0})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 150.dp)
                        ) {
                            items(selectedScreen?.elements ?: emptyList()) { element ->
                                ListItem(
                                    headlineContent = { Text(element.type.displayName) },
                                    supportingContent = { Text(element.text.ifEmpty { "No text" }) },
                                    leadingContent = {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .background(Color(element.backgroundColor), RoundedCornerShape(4.dp))
                                        )
                                    },
                                    trailingContent = {
                                        IconButton(onClick = {
                                            selectedScreen = selectedScreen?.copy(
                                                elements = selectedScreen?.elements?.filter { it.id != element.id } ?: emptyList()
                                            )
                                            screens = screens.map { 
                                                if (it.id == selectedScreen?.id) selectedScreen!! else it 
                                            }
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Add Element Dialog
        if (showElementDialog) {
            AlertDialog(
                onDismissRequest = { showElementDialog = false },
                title = { Text("Add Element") },
                text = {
                    Column {
                        UIElementType.entries.forEach { type ->
                            ListItem(
                                headlineContent = { Text(type.displayName) },
                                modifier = Modifier.clickable {
                                    val newElement = UIElement(type = type, text = type.displayName)
                                    selectedScreen = selectedScreen?.copy(
                                        elements = selectedScreen?.elements?.plus(newElement) ?: listOf(newElement)
                                    )
                                    screens = screens.map { 
                                        if (it.id == selectedScreen?.id) selectedScreen!! else it 
                                    }
                                    showElementDialog = false
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showElementDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
