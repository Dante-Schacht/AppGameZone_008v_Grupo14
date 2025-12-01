package com.example.appgamezone_008v_grupo14.domain.validators

import android.util.Patterns

object Validators {

    fun validateFullName(name: String): String? {
        if (name.isBlank()) {
            return "El nombre no puede estar vacío."
        }
        if (name.length > 100) {
            return "El nombre no puede tener más de 100 caracteres."
        }
        if (!name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))) {
            return "El nombre solo puede contener letras y espacios."
        }
        return null
    }

    fun validateEmail(email: String): String? {
        if (email.isBlank()) {
            return "El correo no puede estar vacío."
        }
        if (email.length > 60) {
            return "El correo no puede tener más de 60 caracteres."
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "El formato del correo no es válido."
        }
        if (!email.endsWith("@duoc.cl")) {
            return "El correo debe ser de dominio @duoc.cl."
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.length < 10) {
            return "La contraseña debe tener al menos 10 caracteres."
        }
        if (!password.contains(Regex("[A-Z]"))) {
            return "Debe incluir al menos una letra mayúscula."
        }
        if (!password.contains(Regex("[a-z]"))) {
            return "Debe incluir al menos una letra minúscula."
        }
        if (!password.contains(Regex("[0-9]"))) {
            return "Debe incluir al menos un número."
        }
        if (!password.contains(Regex("[@#$%.]"))) {
            return "Debe incluir al menos un carácter especial (@#$%.)."
        }
        return null
    }

    fun validateConfirmPassword(password: String, confirm: String): String? {
        if (password != confirm) {
            return "Las contraseñas no coinciden."
        }
        return null
    }

    fun validatePhone(phone: String): String? {
        if (phone.isNotBlank()) {
            if (!phone.matches(Regex("^[0-9]+$"))) {
                return "El teléfono solo debe contener números."
            }
            if (phone.length != 9) {
                return "El teléfono debe tener 9 dígitos."
            }
        }
        return null
    }
}