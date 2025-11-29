package com.example.appgamezone_008v_grupo14.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgamezone_008v_grupo14.data.Prefs
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.domain.validators.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

data class AuthUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val genres: List<String> = emptyList(),
    val avatarUri: String? = null,
    val password: String = "",
    val confirm: String = "",
    val errors: Map<String, String?> = emptyMap(),
    val loading: Boolean = false,
    val loggedIn: Boolean = false,
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = Prefs(app)
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun toggleGenre(g: String) {
        val current = _state.value.genres.toMutableList()
        if (current.contains(g)) current.remove(g) else current.add(g)
        _state.value = _state.value.copy(genres = current)
    }

    fun update(field: String, value: String) {
        _state.value = when (field) {
            "name" -> _state.value.copy(fullName = value)
            "email" -> _state.value.copy(email = value)
            "phone" -> _state.value.copy(phone = value)
            "password" -> _state.value.copy(password = value)
            "confirm" -> _state.value.copy(confirm = value)
            else -> _state.value
        }
    }

    fun setAvatar(uri: String?) { _state.value = _state.value.copy(avatarUri = uri) }

    fun register() {
        val s = _state.value
        val errs = mapOf(
            "name" to Validators.validateName(s.fullName),
            "email" to Validators.validateEmail(s.email),
            "password" to Validators.validatePassword(s.password),
            "confirm" to Validators.validateConfirm(s.password, s.confirm),
            "genres" to Validators.validateGenres(s.genres),
        ).filter { it.value != null }

        if (errs.isNotEmpty()) {
            _state.value = _state.value.copy(errors = errs)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, errors = emptyMap())
            val user = User(s.fullName, s.email, s.phone.ifBlank { null }, s.genres, s.avatarUri)
            val json = JSONObject().apply {
                put("fullName", user.fullName)
                put("email", user.email)
                put("phone", user.phone)
                put("genres", user.genres.joinToString(","))
                put("avatarUri", user.avatarUri)
            }.toString()

            prefs.saveUser(json)
            prefs.saveToken("local-token")
            _state.value = _state.value.copy(loading = false, loggedIn = true)
        }
    }

    fun login(email: String, password: String, onError: (String) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onError("Debes rellenar todos los campos.")
            return
        }

        viewModelScope.launch {
            val saved = prefs.userFlow().first()
            var ok = false
            if (saved != null) {
                try {
                    val obj = JSONObject(saved)
                    ok = (obj.getString("email") == email) && password.length >= 10
                } catch(e: Exception) {
                    ok = false
                }
            }
            if (!ok) {
                onError("Usuario o contraseña inválidos.")
            } else {
                _state.value = _state.value.copy(loggedIn = true)
            }
        }
    }
    
    fun onLoginSuccess() {
        // Placeholder for any logic needed after successful login
    }
}
