package com.minecraftmodcreator.ui.screens.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraftmodcreator.domain.model.*
import com.minecraftmodcreator.domain.repository.ModRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class EditorUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val mod: Mod = Mod(),
    val isNewMod: Boolean = true,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val modRepository: ModRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val modId: String? = savedStateHandle.get<String>("modId")?.takeIf { it != "new" }

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    init {
        loadMod()
    }

    private fun loadMod() {
        viewModelScope.launch {
            if (modId != null) {
                val mod = modRepository.getModById(modId)
                if (mod != null) {
                    _uiState.value = EditorUiState(
                        isLoading = false,
                        mod = mod,
                        isNewMod = false
                    )
                } else {
                    _uiState.value = EditorUiState(
                        isLoading = false,
                        error = "Mod not found"
                    )
                }
            } else {
                _uiState.value = EditorUiState(
                    isLoading = false,
                    isNewMod = true
                )
            }
        }
    }

    fun updateModName(name: String) {
        _uiState.value = _uiState.value.copy(
            mod = _uiState.value.mod.copy(name = name)
        )
    }

    fun updateModDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            mod = _uiState.value.mod.copy(description = description)
        )
    }

    fun updateModVersion(version: String) {
        _uiState.value = _uiState.value.copy(
            mod = _uiState.value.mod.copy(version = version)
        )
    }

    fun addElement(element: ModElement) {
        val currentElements = _uiState.value.mod.elements.toMutableList()
        currentElements.add(element)
        _uiState.value = _uiState.value.copy(
            mod = _uiState.value.mod.copy(elements = currentElements)
        )
    }

    fun updateElement(element: ModElement) {
        val currentElements = _uiState.value.mod.elements.toMutableList()
        val index = currentElements.indexOfFirst { it.id == element.id }
        if (index != -1) {
            currentElements[index] = element
            _uiState.value = _uiState.value.copy(
                mod = _uiState.value.mod.copy(elements = currentElements)
            )
        }
    }

    fun deleteElement(elementId: String) {
        val currentElements = _uiState.value.mod.elements.toMutableList()
        currentElements.removeAll { it.id == elementId }
        _uiState.value = _uiState.value.copy(
            mod = _uiState.value.mod.copy(elements = currentElements)
        )
    }

    fun saveMod() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            try {
                val mod = _uiState.value.mod
                val modToSave = if (_uiState.value.isNewMod) {
                    mod.copy(
                        id = UUID.randomUUID().toString(),
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                } else {
                    mod.copy(updatedAt = System.currentTimeMillis())
                }
                modRepository.saveMod(modToSave)
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    mod = modToSave,
                    isNewMod = false,
                    saveSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Failed to save mod"
                )
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
