package com.example.appgamezone_008v_grupo14.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgamezone_008v_grupo14.ui.components.AppTopBar
import com.example.appgamezone_008v_grupo14.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onLogged: () -> Unit, onBack: () -> Unit, onGoRegister: () -> Unit) {
    val vm: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = { AppTopBar("Login", onBack) }) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth())

            AnimatedVisibility(error != null, enter = fadeIn(), exit = fadeOut()) {
                Text(error ?: "", color = MaterialTheme.colorScheme.error)
            }

            Button(onClick = { vm.login(email, pass, onSuccess = onLogged, onError = { error = it }) },
                modifier = Modifier.fillMaxWidth()) {
                Text("Ingresar")
            }

            TextButton(onClick = onGoRegister) { Text("¿No tienes cuenta? Regístrate") }
        }
    }
}
