package com.example.appgamezone_008v_grupo14.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.data.UserRepository
import com.example.appgamezone_008v_grupo14.viewModels.RegisterViewModel

@Composable
fun RegisterScreen(
    onGoToLogin: () -> Unit,
    vm: RegisterViewModel = remember { RegisterViewModel() }
) {
    val scroll = rememberScrollState()
    val context = LocalContext.current
    val repo = remember { UserRepository(context) }

    val genresList = listOf("FICCIÓN","NO FICCIÓN","MISTERIO","TERROR","SUSPENSO","HISTORIA")

    if (vm.registerSuccess.value) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Registro Exitoso") },
            text = { Text("Tu cuenta ha sido creada correctamente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.registerSuccess.value = false
                        onGoToLogin()
                    }
                ) { Text("Ir a Iniciar Sesión") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = vm.fullName.value,
            onValueChange = { vm.fullName.value = it },
            label = { Text("Nombre completo (obligatorio)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        vm.fullNameError.value?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        OutlinedTextField(
            value = vm.email.value,
            onValueChange = { vm.email.value = it },
            label = { Text("Correo (@duoc.cl)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        vm.emailError.value?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        OutlinedTextField(
            value = vm.password.value,
            onValueChange = { vm.password.value = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        vm.passwordError.value?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        OutlinedTextField(
            value = vm.confirmPassword.value,
            onValueChange = { vm.confirmPassword.value = it },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        vm.confirmPasswordError.value?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        OutlinedTextField(
            value = vm.phone.value,
            onValueChange = { vm.phone.value = it },
            label = { Text("Teléfono (opcional)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        vm.phoneError.value?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Text("Géneros favoritos:", style = MaterialTheme.typography.titleMedium)
            genresList.forEach { genre ->
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = vm.selectedGenres.value.contains(genre),
                        onCheckedChange = { checked ->
                            val now = vm.selectedGenres.value.toMutableSet()
                            if (checked) now.add(genre) else now.remove(genre)
                            vm.selectedGenres.value = now
                        }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = genre,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable {
                            val now = vm.selectedGenres.value.toMutableSet()
                            if (now.contains(genre)) now.remove(genre) else now.add(genre)
                            vm.selectedGenres.value = now
                        }
                    )
                }
            }
            vm.genresError.value?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }
        }

        vm.registerServerError.value?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall) }

        Button(
            onClick = {
                val user: User? = vm.validateAndBuildUser()
                if (user != null) {
                    if (repo.isEmailAlreadyRegistered(user.email)) {
                        vm.registerServerError.value = "Ese correo ya está registrado en este dispositivo."
                    } else {
                        // Log visual opcional: Snackbar/Toast (no requerido)
                        repo.saveUser(user)
                        vm.registerSuccess.value = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (vm.loading.value) "Creando cuenta..." else "REGISTRAR")
        }

        TextButton(onClick = { onGoToLogin() }, modifier = Modifier.fillMaxWidth()) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
