package com.minecraftmodcreator.ui.screens.mods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraftmodcreator.domain.model.Mod
import com.minecraftmodcreator.domain.repository.ModRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModsUiState(
    val isLoading: Boolean = true,
    val mods: List<Mod> = emptyList(),
    val searchQuery: String = ""
)

@HiltViewModel
class ModsViewModel @Inject constructor(
    private val modRepository: ModRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModsUiState())
    val uiState: StateFlow<ModsUiState> = _uiState.asStateFlow()

    init {
        loadMods()
    }

    private fun loadMods() {
        viewModelScope.launch {
            modRepository.getAllMods().collect { mods ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    mods = mods
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        viewModelScope.launch {
            if (query.isBlank()) {
                loadMods()
            } else {
                modRepository.searchMods(query).collect { mods ->
                    _uiState.value = _uiState.value.copy(mods = mods)
                }
            }
        }
    }

    fun deleteMod(modId: String) {
        viewModelScope.launch {
            modRepository.deleteMod(modId)
        }
    }

    fun duplicateMod(modId: String) {
        viewModelScope.launch {
            modRepository.duplicateMod(modId)
        }
    }
}
