package com.example.appgamezone_008v_grupo14.domain.validators

object Validators {
    private val nameRegex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,100}$")
    private val duocRegex = Regex("^[A-Za-z0-9._%+-]+@duoc\\.cl$")
    private val upper = Regex("[A-ZÁÉÍÓÚÑ]")
    private val lower = Regex("[a-záéíóúñ]")
    private val digit = Regex("\\d")
    private val special = Regex("[@#\$%&*!?._-]")

    fun validateName(name: String) = when {
        name.isBlank() -> "El nombre no puede estar vacío."
        !nameRegex.matches(name) -> "Solo letras y espacios (máx. 100)."
        else -> null
    }

    fun validateEmail(email: String) = when {
        !duocRegex.matches(email) -> "Usa un correo @duoc.cl válido."
        else -> null
    }

    fun validatePassword(pw: String) = when {
        pw.length < 10 -> "Mínimo 10 caracteres."
        !upper.containsMatchIn(pw) -> "Debe incluir mayúscula."
        !lower.containsMatchIn(pw) -> "Debe incluir minúscula."
        !digit.containsMatchIn(pw) -> "Debe incluir número."
        !special.containsMatchIn(pw) -> "Debe incluir carácter especial (@#%...)."
        else -> null
    }

    fun validateConfirm(pw: String, confirm: String) =
        if (pw != confirm) "Las contraseñas no coinciden." else null

    fun validateGenres(genres: List<String>) =
        if (genres.isEmpty()) "Selecciona al menos un género." else null
}
