package com.minecraftmodcreator.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minecraftmodcreator.domain.model.Mod
import com.minecraftmodcreator.domain.repository.ModRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val modCount: Int = 0,
    val itemCount: Int = 0,
    val recentMods: List<Mod> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val modRepository: ModRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                modRepository.getModCount(),
                modRepository.getRecentMods(5)
            ) { count, recentMods ->
                val itemCount = recentMods.sumOf { it.elements.size }
                HomeUiState(
                    isLoading = false,
                    modCount = count,
                    itemCount = itemCount,
                    recentMods = recentMods
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
