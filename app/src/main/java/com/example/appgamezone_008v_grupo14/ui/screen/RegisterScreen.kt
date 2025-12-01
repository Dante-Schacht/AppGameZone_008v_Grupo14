package com.example.appgamezone_008v_grupo14.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appgamezone_008v_grupo14.viewModels.AuthViewModel
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.domain.validators.Validators

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(authViewModel: AuthViewModel, onNavigateToLogin: () -> Unit) {
    // Form fields states
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedGenres by remember { mutableStateOf<List<String>>(emptyList()) }

    // Visibility states
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Error states
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var genreError by remember { mutableStateOf<String?>(null) }

    val registrationResult by authViewModel.registrationResult.collectAsState()
    val apiError by authViewModel.error.collectAsState()

    val genres = listOf(
        "Acción",
        "Aventura",
        "RPG",
        "Estrategia",
        "Deportes",
        "Simulación",
        "Puzzle",
        "Terror"
    )

    val isFormValid by remember(fullName, email, password, confirmPassword, phone, selectedGenres) {
        derivedStateOf {
            Validators.validateFullName(fullName) == null &&
            Validators.validateEmail(email) == null &&
            Validators.validatePassword(password) == null &&
            Validators.validateConfirmPassword(password, confirmPassword) == null &&
            Validators.validatePhone(phone) == null &&
            fullName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            selectedGenres.isNotEmpty()
        }
    }

    LaunchedEffect(registrationResult) {
        if (registrationResult) {
            onNavigateToLogin()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Text("Registro", style = MaterialTheme.typography.headlineMedium) }
        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Form Fields
        item { 
            OutlinedTextField(
                value = fullName,
                onValueChange = { 
                    fullName = it
                    fullNameError = Validators.validateFullName(it)
                },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                isError = fullNameError != null,
                supportingText = { fullNameError?.let { Text(it) } }
            )
        }
        item { 
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it 
                    emailError = Validators.validateEmail(it)
                },
                label = { Text("Correo Electrónico (@duoc.cl)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } }
            )
        }
        item {
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it 
                    passwordError = Validators.validatePassword(it)
                    confirmPasswordError = Validators.validateConfirmPassword(it, confirmPassword)
                },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it) } }
            )
        }
        item {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    confirmPasswordError = Validators.validateConfirmPassword(password, it)
                },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = confirmPasswordError != null,
                supportingText = { confirmPasswordError?.let { Text(it) } }
            )
        }
        item {
            OutlinedTextField(
                value = phone,
                onValueChange = { 
                    if (it.length <= 9) phone = it
                    phoneError = Validators.validatePhone(it)
                },
                label = { Text("Teléfono (Opcional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                isError = phoneError != null,
                supportingText = { phoneError?.let { Text(it) } }
            )
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { Text("Género Favorito", style = MaterialTheme.typography.titleMedium) }
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Animated Genre Selection
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                genres.forEach { genre ->
                    FilterChip(
                        selected = genre in selectedGenres,
                        onClick = { 
                            selectedGenres = if (genre in selectedGenres) {
                                selectedGenres - genre
                            } else {
                                selectedGenres + genre
                            }
                            genreError = null
                        },
                        label = { Text(genre) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
        item { genreError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 4.dp)) } }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            Button(
                onClick = {
                    // Re-validate on click just in case
                    fullNameError = Validators.validateFullName(fullName)
                    emailError = Validators.validateEmail(email)
                    passwordError = Validators.validatePassword(password)
                    confirmPasswordError = Validators.validateConfirmPassword(password, confirmPassword)
                    phoneError = Validators.validatePhone(phone)
                    if (selectedGenres.isEmpty()) genreError = "Debes seleccionar al menos un género."
                    
                    if (isFormValid) {
                        val user = User(
                            fullName = fullName,
                            email = email,
                            password = password,
                            phone = phone.takeIf { it.isNotBlank() },
                            genres = selectedGenres.joinToString(",")
                        )
                        authViewModel.register(user)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Registrarse")
            }
        }

        item {
            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Iniciar Sesión")
            }
        }

        item { 
            apiError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}