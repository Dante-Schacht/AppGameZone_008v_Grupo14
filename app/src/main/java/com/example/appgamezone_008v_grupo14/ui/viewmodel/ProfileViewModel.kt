package com.example.appgamezone_008v_grupo14.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgamezone_008v_grupo14.data.Prefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

data class ProfileState(
    val fullName: String = "",
    val email: String = "",
    val phone: String? = null,
    val genres: List<String> = emptyList(),
    val avatarUri: String? = null
)

class ProfileViewModel(app: Application): AndroidViewModel(app) {
    private val prefs = Prefs(app)
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        viewModelScope.launch {
            val saved = prefs.userFlow().first()
            saved?.let {
                try {
                    val o = JSONObject(it)
                    _state.value = ProfileState(
                        fullName = o.optString("fullName"),
                        email = o.optString("email"),
                        phone = o.optString("phone").ifBlank { null },
                        genres = o.optString("genres").split(",").filter { it.isNotBlank() },
                        avatarUri = o.optString("avatarUri").ifBlank { null }
                    )
                } catch (e: Exception) {
                    // Handle JSON parsing error
                }
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            prefs.clear()
            onDone()
        }
    }

    fun updateAvatar(uri: String?) {
        _state.value = _state.value.copy(avatarUri = uri)
        saveGenres()
    }

    fun toggleGenre(genre: String) {
        val currentGenres = _state.value.genres.toMutableList()
        if (currentGenres.contains(genre)) {
            currentGenres.remove(genre)
        } else {
            currentGenres.add(genre)
        }
        _state.value = _state.value.copy(genres = currentGenres)
        saveGenres()
    }

    private fun saveGenres() {
        viewModelScope.launch {
            val current = JSONObject().apply {
                put("fullName", _state.value.fullName)
                put("email", _state.value.email)
                put("phone", _state.value.phone)
                put("genres", _state.value.genres.joinToString(","))
                put("avatarUri", _state.value.avatarUri)
            }.toString()
            prefs.saveUser(current)
        }
    }
}
