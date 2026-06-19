package com.minecraftmodcreator.ui.screens.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minecraftmodcreator.domain.model.ModType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModTypeSelectionScreen(
    onBack: () -> Unit,
    onModTypeSelected: (ModType) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Mod Type") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "What type of mod do you want to create?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(ModType.entries) { modType ->
                    ModTypeCard(
                        modType = modType,
                        onClick = { onModTypeSelected(modType) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ModTypeCard(
    modType: ModType,
    onClick: () -> Unit
) {
    val colors = when (modType) {
        ModType.BASIC -> listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
        ModType.TEXTURE -> listOf(Color(0xFFE91E63), Color(0xFFAD1457))
        ModType.INTERFACE -> listOf(Color(0xFF2196F3), Color(0xFF0D47A1))
        ModType.XRAY -> listOf(Color(0xFF9C27B0), Color(0xFF6A1B9A))
        ModType.GLOW -> listOf(Color(0xFFFFEB3B), Color(0xFFF57F17))
        ModType.ACTION -> listOf(Color(0xFFFF5722), Color(0xFFBF360C))
        ModType.COMPLETE -> listOf(Color(0xFF00BCD4), Color(0xFF00838F))
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = colors)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = modType.emoji,
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = modType.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = modType.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
