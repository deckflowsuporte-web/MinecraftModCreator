package com.minecraftmodcreator.ui.screens.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraftmodcreator.domain.model.*
import com.minecraftmodcreator.domain.repository.ModRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

data class ExportUiState(
    val isLoading: Boolean = true,
    val isExporting: Boolean = false,
    val mod: Mod? = null,
    val exportSuccess: Boolean = false,
    val exportError: String? = null,
    val exportedFilePath: String? = null
)

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val modRepository: ModRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val modId: String = savedStateHandle.get<String>("modId") ?: ""

    private val _uiState = MutableStateFlow(ExportUiState())
    val uiState: StateFlow<ExportUiState> = _uiState.asStateFlow()

    init {
        loadMod()
    }

    private fun loadMod() {
        viewModelScope.launch {
            val mod = modRepository.getModById(modId)
            _uiState.value = ExportUiState(
                isLoading = false,
                mod = mod
            )
        }
    }

    fun exportAsDatapack() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true, exportError = null)
            try {
                val mod = _uiState.value.mod ?: throw Exception("Mod not found")
                val file = withContext(Dispatchers.IO) {
                    createDatapackZip(mod)
                }
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportSuccess = true,
                    exportedFilePath = file.absolutePath
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportError = e.message ?: "Export failed"
                )
            }
        }
    }

    fun exportAsResourcePack() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true, exportError = null)
            try {
                val mod = _uiState.value.mod ?: throw Exception("Mod not found")
                val file = withContext(Dispatchers.IO) {
                    createResourcePackZip(mod)
                }
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportSuccess = true,
                    exportedFilePath = file.absolutePath
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportError = e.message ?: "Export failed"
                )
            }
        }
    }

    private fun createDatapackZip(mod: Mod): File {
        val namespace = mod.name.lowercase().replace(" ", "_").replace(Regex("[^a-z0-9_]"), "")
        val tempDir = File(context.cacheDir, "export_${System.currentTimeMillis()}")
        tempDir.mkdirs()

        // Create pack.mcmeta
        val packMcmeta = """
            {
                "pack": {
                    "pack_format": 15,
                    "description": "${mod.name} - ${mod.description}"
                }
            }
        """.trimIndent()
        File(tempDir, "pack.mcmeta").writeText(packMcmeta)

        // Create data folder structure
        val dataDir = File(tempDir, "data/$namespace")
        dataDir.mkdirs()

        // Generate loot tables, recipes, tags
        mod.elements.forEach { element ->
            when (element.type) {
                ModElementType.ITEM -> {
                    val lootTableDir = File(dataDir, "loot_tables/blocks")
                    lootTableDir.mkdirs()
                    val lootTable = createLootTable(element)
                    File(lootTableDir, "${element.id}.json").writeText(lootTable)
                }
                ModElementType.BLOCK -> {
                    val lootTableDir = File(dataDir, "loot_tables/blocks")
                    lootTableDir.mkdirs()
                    val lootTable = createLootTable(element)
                    File(lootTableDir, "${element.id}.json").writeText(lootTable)
                }
                ModElementType.RECIPE -> {
                    val recipeDir = File(dataDir, "recipes")
                    recipeDir.mkdirs()
                    val recipe = createRecipe(element)
                    File(recipeDir, "${element.id}.json").writeText(recipe)
                }
                else -> { /* Other types handled in resource pack */ }
            }
        }

        // Create tags
        val tagsDir = File(dataDir, "tags/items")
        tagsDir.mkdirs()
        val items = mod.elements.filter { it.type == ModElementType.ITEM }
        if (items.isNotEmpty()) {
            val tagValues = items.map { "\"${namespace}:${it.id}\"" }.joinToString(",\n")
            val tag = """
                {
                    "values": [
                        $tagValues
                    ]
                }
            """.trimIndent()
            File(tagsDir, "custom_items.json").writeText(tag)
        }

        // Create zip
        val zipFile = File(context.cacheDir, "${mod.name.replace(" ", "_")}_datapack.zip")
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            addFolderToZip(tempDir, tempDir, zos)
        }

        // Cleanup
        tempDir.deleteRecursively()

        return zipFile
    }

    private fun createResourcePackZip(mod: Mod): File {
        val namespace = mod.name.lowercase().replace(" ", "_").replace(Regex("[^a-z0-9_]"), "")
        val tempDir = File(context.cacheDir, "export_${System.currentTimeMillis()}")
        tempDir.mkdirs()

        // Create pack.mcmeta
        val packMcmeta = """
            {
                "pack": {
                    "pack_format": 15,
                    "description": "${mod.name} - ${mod.description}"
                }
            }
        """.trimIndent()
        File(tempDir, "pack.mcmeta").writeText(packMcmeta)

        // Create assets folder structure
        val assetsDir = File(tempDir, "assets/$namespace")
        
        // Create item models
        val modelsDir = File(assetsDir, "models/item")
        modelsDir.mkdirs()
        mod.elements.filter { it.type == ModElementType.ITEM || it.type == ModElementType.TOOL || it.type == ModElementType.ARMOR }.forEach { element ->
            val model = createItemModel(element)
            File(modelsDir, "${element.id}.json").writeText(model)
        }

        // Create block models
        val blockModelsDir = File(assetsDir, "models/block")
        blockModelsDir.mkdirs()
        mod.elements.filter { it.type == ModElementType.BLOCK }.forEach { element ->
            val model = createBlockModel(element)
            File(blockModelsDir, "${element.id}.json").writeText(model)
        }

        // Create textures (placeholder)
        val texturesDir = File(assetsDir, "textures/item")
        texturesDir.mkdirs()
        // Note: In a real app, you would create actual texture files

        // Create lang file
        val langDir = File(assetsDir, "lang")
        langDir.mkdirs()
        val langEntries = mod.elements.joinToString("\n") { 
            "\"${namespace}:${it.id}\": \"${it.name}\""
        }
        File(langDir, "en_us.json").writeText("{\n$langEntries\n}")

        // Create entity models for mobs
        val entityDir = File(assetsDir, "models/entity")
        entityDir.mkdirs()
        mod.elements.filter { it.type == ModElementType.MOB }.forEach { element ->
            val entityModel = createEntityModel(element)
            File(entityDir, "${element.id}.json").writeText(entityModel)
        }

        // Create zip
        val zipFile = File(context.cacheDir, "${mod.name.replace(" ", "_")}_resourcepack.zip")
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            addFolderToZip(tempDir, tempDir, zos)
        }

        // Cleanup
        tempDir.deleteRecursively()

        return zipFile
    }

    private fun createLootTable(element: ModElement): String {
        val namespace = _uiState.value.mod?.name?.lowercase()?.replace(" ", "_") ?: "mod"
        return """
            {
                "type": "minecraft:block",
                "pools": [
                    {
                        "rolls": 1,
                        "entries": [
                            {
                                "type": "minecraft:item",
                                "name": "$namespace:${element.id}"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()
    }

    private fun createRecipe(element: ModElement): String {
        val namespace = _uiState.value.mod?.name?.lowercase()?.replace(" ", "_") ?: "mod"
        val resultItem = element.properties["resultItem"] ?: element.id
        val resultAmount = element.properties["resultAmount"] ?: "1"
        val recipeType = element.properties["recipeType"] ?: "shaped"

        return when (recipeType) {
            "shaped" -> """
                {
                    "type": "minecraft:crafting_shaped",
                    "pattern": [
                        "###",
                        "###",
                        "###"
                    ],
                    "key": {
                        "#": {
                            "item": "minecraft:diamond"
                        }
                    },
                    "result": {
                        "item": "$namespace:$resultItem",
                        "count": $resultAmount
                    }
                }
            """.trimIndent()
            "shapeless" -> """
                {
                    "type": "minecraft:crafting_shapeless",
                    "ingredients": [
                        {
                            "item": "minecraft:diamond"
                        }
                    ],
                    "result": {
                        "item": "$namespace:$resultItem",
                        "count": $resultAmount
                    }
                }
            """.trimIndent()
            else -> """
                {
                    "type": "minecraft:smelting",
                    "ingredient": {
                        "item": "minecraft:diamond"
                    },
                    "result": "$namespace:$resultItem",
                    "experience": 1.0,
                    "cookingtime": 200
                }
            """.trimIndent()
        }
    }

    private fun createItemModel(element: ModElement): String {
        return """
            {
                "parent": "minecraft:item/generated",
                "textures": {
                    "layer0": "${_uiState.value.mod?.name?.lowercase()?.replace(" ", "_")}:item/${element.id}"
                }
            }
        """.trimIndent()
    }

    private fun createBlockModel(element: ModElement): String {
        return """
            {
                "parent": "minecraft:block/cube_all",
                "textures": {
                    "all": "${_uiState.value.mod?.name?.lowercase()?.replace(" ", "_")}:block/${element.id}"
                }
            }
        """.trimIndent()
    }

    private fun createEntityModel(element: ModElement): String {
        val health = element.properties["health"]?.toFloatOrNull() ?: 20f
        val healthBars = (health / 4).toInt()
        return """
            {
                "format_version": "1.8.0",
                "minecraft:client_entity": {
                    "description": {
                        "identifier": "${_uiState.value.mod?.name?.lowercase()?.replace(" ", "_")}:${element.id}",
                        "textures": {
                            "default": "${_uiState.value.mod?.name?.lowercase()?.replace(" ", "_")}:textures/entity/${element.id}.png"
                        }
                    },
                    "components": {
                        "minecraft:health": {
                            "value": $health
                        },
                        "minecraft:attack_damage": {
                            "value": ${element.properties["damage"]?.toFloatOrNull() ?: 3f}
                        }
                    }
                }
            }
        """.trimIndent()
    }

    private fun addFolderToZip(folder: File, baseFolder: File, zos: ZipOutputStream) {
        folder.listFiles()?.forEach { file ->
            val entryName = file.relativeTo(baseFolder).path.replace("\\", "/")
            if (file.isDirectory) {
                addFolderToZip(file, baseFolder, zos)
            } else {
                zos.putNextEntry(ZipEntry(entryName))
                file.inputStream().use { input ->
                    input.copyTo(zos)
                }
                zos.closeEntry()
            }
        }
    }

    fun getShareIntent(): Intent? {
        val filePath = _uiState.value.exportedFilePath ?: return null
        val file = File(filePath)
        if (!file.exists()) return null

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        return Intent(Intent.ACTION_SEND).apply {
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun clearExportState() {
        _uiState.value = _uiState.value.copy(exportSuccess = false, exportedFilePath = null)
    }
}
