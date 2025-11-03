package com.example.appgamezone_008v_grupo14.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.appgamezone_008v_grupo14.data.User

class RegisterViewModel : ViewModel() {

    // Campos del formulario
    val fullName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val phone = mutableStateOf("")
    val selectedGenres = mutableStateOf(setOf<String>())

    // Errores por campo
    val fullNameError = mutableStateOf<String?>(null)
    val emailError = mutableStateOf<String?>(null)
    val passwordError = mutableStateOf<String?>(null)
    val confirmPasswordError = mutableStateOf<String?>(null)
    val phoneError = mutableStateOf<String?>(null)
    val genresError = mutableStateOf<String?>(null)

    // Estado de resultado
    val registerSuccess = mutableStateOf(false)
    val registerServerError = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    // (Mantén tu onRegisterClick si lo usas para UI; no guarda nada)
    fun onRegisterClick() { /* opcional: ya no se usa para guardar */ }

    fun validateAndBuildUser(): User? {
        loading.value = true
        clearErrors()
        var hasError = false

        if (fullName.value.isBlank()) {
            fullNameError.value = "Ingresa tu nombre completo."
            hasError = true
        }

        if (email.value.isBlank()) {
            emailError.value = "El correo es obligatorio."
            hasError = true
        } else if (!email.value.matches(Regex("^[A-Za-z0-9._%+-]+@duoc\\.cl$"))) {
            emailError.value = "Debe ser un correo @duoc.cl válido."
            hasError = true
        }

        val pass = password.value
        val passOk = pass.length >= 10 &&
                pass.any { it.isUpperCase() } &&
                pass.any { it.isLowerCase() } &&
                pass.any { it.isDigit() } &&
                pass.any { !it.isLetterOrDigit() }
        if (!passOk) {
            passwordError.value = "La contraseña debe tener 10+ caracteres, mayúscula, minúscula, número y símbolo."
            hasError = true
        }

        if (confirmPassword.value != password.value) {
            confirmPasswordError.value = "Las contraseñas no coinciden."
            hasError = true
        }

        if (phone.value.isNotBlank() && !phone.value.matches(Regex("^[0-9+() -]{6,}$"))) {
            phoneError.value = "Número inválido."
            hasError = true
        }

        if (selectedGenres.value.isEmpty()) {
            genresError.value = "Selecciona al menos un género favorito."
            hasError = true
        }

        if (hasError) {
            registerServerError.value = "No se pudo registrar. Revisa los campos."
            loading.value = false
            return null
        }

        loading.value = false
        return User(
            fullName = fullName.value.trim(),
            email = email.value.trim(),
            password = password.value,
            phone = phone.value.ifBlank { null },
            genres = selectedGenres.value.toList()
        )
    }

    fun clearErrors() {
        fullNameError.value = null
        emailError.value = null
        passwordError.value = null
        confirmPasswordError.value = null
        phoneError.value = null
        genresError.value = null
        registerServerError.value = null
    }
}
