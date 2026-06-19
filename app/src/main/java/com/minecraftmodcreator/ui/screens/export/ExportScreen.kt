package com.minecraftmodcreator.ui.screens.export

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.minecraftmodcreator.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    modId: String,
    onNavigateBack: () -> Unit,
    viewModel: ExportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.exportSuccess) {
        if (uiState.exportSuccess) {
            snackbarHostState.showSnackbar("Export successful!")
        }
    }

    LaunchedEffect(uiState.exportError) {
        uiState.exportError?.let {
            snackbarHostState.showSnackbar("Export failed: $it")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export Mod") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.mod?.let { mod ->
                    // Mod Info Header
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📦",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = mod.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${mod.elements.size} elements • v${mod.version}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Export Options
                    Text(
                        text = "Export Options",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    ExportOptionCard(
                        icon = Icons.Default.DataObject,
                        emoji = "🗃️",
                        title = "Datapack",
                        description = "Adds items, recipes, and loot tables. Safe for achievements!",
                        color = MinecraftBlue,
                        isLoading = uiState.isExporting,
                        onClick = { viewModel.exportAsDatapack() }
                    )

                    ExportOptionCard(
                        icon = Icons.Default.Palette,
                        emoji = "🎨",
                        title = "Resource Pack",
                        description = "Adds textures, models, and language files.",
                        color = MinecraftBrown,
                        isLoading = uiState.isExporting,
                        onClick = { viewModel.exportAsResourcePack() }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Share Button
                    if (uiState.exportSuccess && uiState.exportedFilePath != null) {
                        Button(
                            onClick = {
                                viewModel.getShareIntent()?.let { intent ->
                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Share Export File")
                        }
                    }

                    // Info Note
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "How to use",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "1. Export your mod as a datapack or resource pack\n" +
                                            "2. Share the file to your device\n" +
                                            "3. Place in Minecraft's datapacks/resourcepacks folder\n" +
                                            "4. Enable in world settings and enjoy!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExportOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    emoji: String,
    title: String,
    description: String,
    color: androidx.compose.ui.graphics.Color,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = !isLoading
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = color
                    )
                } else {
                    Text(
                        text = emoji,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
