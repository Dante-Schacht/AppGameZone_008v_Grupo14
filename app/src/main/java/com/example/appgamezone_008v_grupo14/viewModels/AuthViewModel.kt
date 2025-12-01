package com.example.appgamezone_008v_grupo14.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Holds the currently logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // State for login result (transient)
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    // State for registration result (transient)
    private val _registrationResult = MutableStateFlow(false)
    val registrationResult: StateFlow<Boolean> = _registrationResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.login(email, password)
            _currentUser.value = user
            _loginSuccess.value = user != null
            if (user == null) {
                _error.value = "Usuario o contraseña incorrectos."
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            val existingUser = userRepository.getUserByEmail(user.email)
            if (existingUser != null) {
                _error.value = "El correo electrónico ya está registrado."
                _registrationResult.value = false
            } else {
                userRepository.insertUser(user)
                _registrationResult.value = true
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            _currentUser.value = user // Update the user state in the app
        }
    }

    fun logout() {
        _currentUser.value = null
        _loginSuccess.value = false // Reset login state
    }

    fun resetError() {
        _error.value = null
    }
    
    fun resetLoginStatus(){
        _loginSuccess.value = false
    }

    fun resetRegistrationResult() {
        _registrationResult.value = false
    }
}