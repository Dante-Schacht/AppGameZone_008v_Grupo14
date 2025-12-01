package com.example.appgamezone_008v_grupo14.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgamezone_008v_grupo14.network.Game
import com.example.appgamezone_008v_grupo14.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GameUiState {
    object Loading : GameUiState()
    data class Success(val games: List<Game>) : GameUiState()
    data class Error(val message: String) : GameUiState()
}

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _selectedGameState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val selectedGameState: StateFlow<GameUiState> = _selectedGameState.asStateFlow()

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            _uiState.value = GameUiState.Loading
            try {
                val response = RetrofitClient.instance.getGames()
                _uiState.value = GameUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchGameById(id: Int) {
        viewModelScope.launch {
            _selectedGameState.value = GameUiState.Loading
            try {
                val response = RetrofitClient.instance.getGameById(id)
                _selectedGameState.value = GameUiState.Success(listOf(response))
            } catch (e: Exception) {
                _selectedGameState.value = GameUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}