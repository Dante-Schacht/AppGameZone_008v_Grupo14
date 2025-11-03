package com.example.appgamezone_008v_grupo14.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appgamezone_008v_grupo14.data.UserRepository

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { UserRepository(context) }

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        emailError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        passError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        loginError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        Button(
            onClick = {
                emailError = null; passError = null; loginError = null
                var bad = false
                if (email.isBlank()) { emailError = "Ingresa tu correo."; bad = true }
                if (pass.isBlank()) { passError = "Ingresa tu contraseña."; bad = true }

                if (!bad) {
                    val ok = repo.validateCredentials(email.trim(), pass)
                    if (ok) onLoginSuccess()
                    else loginError = "Correo o contraseña incorrectos."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("INGRESAR") }

        TextButton(onClick = { onGoToRegister() }, modifier = Modifier.fillMaxWidth()) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}
