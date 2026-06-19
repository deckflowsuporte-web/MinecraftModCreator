package com.minecraftmodcreator.ui.screens.creator

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextureEditorScreen(
    textureName: String = "",
    initialPixels: List<List<Color>>? = null,
    onBack: () -> Unit,
    onSave: (name: String, pixels: List<List<Color>>) -> Unit
) {
    val gridSize = 16
    var textureNameState by remember { mutableStateOf(textureName) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var pixels by remember {
        mutableStateOf(
            initialPixels ?: List(gridSize) { List(gridSize) { Color.Transparent } }
        )
    }
    
    val colorPalette = listOf(
        Color.White, Color.Black, Color.Red, Color.Green, Color.Blue,
        Color.Yellow, Color.Cyan, Color.Magenta,
        Color(0xFF8B4513), Color(0xFF228B22), Color(0xFF808080),
        Color(0xFFD2691E), Color(0xFFFFD700), Color(0xFFC0C0C0),
        Color(0xFF4169E1), Color(0xFFDC143C), Color(0xFF00FF00),
        Color(0xFFFF6347), Color(0xFF9370DB), Color(0xFF20B2AA),
        Color.Transparent
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Texture Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        pixels = List(gridSize) { List(gridSize) { Color.Transparent } }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear")
                    }
                    IconButton(onClick = { onSave(textureNameState, pixels) }) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = textureNameState,
                onValueChange = { textureNameState = it },
                label = { Text("Texture Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Pixel Grid
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridSize),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(gridSize * gridSize) { index ->
                        val x = index % gridSize
                        val y = index / gridSize
                        val pixelColor = pixels[y][x]
                        
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(
                                    if (pixelColor == Color.Transparent) Color.LightGray
                                    else pixelColor
                                )
                                .border(0.5.dp, Color.Gray.copy(alpha = 0.3f))
                                .clickable {
                                    val newPixels = pixels.toMutableList().map { it.toMutableList() }
                                    newPixels[y][x] = selectedColor
                                    pixels = newPixels
                                }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Selected Color
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Selected: ", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (selectedColor == Color.Transparent) Color.LightGray
                            else selectedColor
                        )
                        .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Color Palette
            Text(
                text = "Color Palette",
                style = MaterialTheme.typography.titleSmall
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(colorPalette) { color ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (color == Color.Transparent) Color.LightGray
                                else color
                            )
                            .border(
                                width = if (selectedColor == color) 3.dp else 1.dp,
                                color = if (selectedColor == color)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable { selectedColor = color }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onSave(textureNameState, pixels) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Texture")
            }
        }
    }
}
