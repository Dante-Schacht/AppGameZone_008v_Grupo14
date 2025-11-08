package com.example.appgamezone_008v_grupo14.ui.screens.auth

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgamezone_008v_grupo14.ui.components.AppTopBar
import com.example.appgamezone_008v_grupo14.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(onRegistered: () -> Unit, onBack: () -> Unit) {
    val vm: AuthViewModel = viewModel()
    val state = vm.state.collectAsState().value

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            val uri = res.data?.data?.toString()
            vm.setAvatar(uri)
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    Scaffold(topBar = { AppTopBar("Registro", onBack) }) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = state.fullName,
                onValueChange = { vm.update("name", it) },
                label = { Text("Nombre completo") },
                isError = state.errors["name"] != null,
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(state.errors["name"] != null, enter = fadeIn(), exit = fadeOut()) {
                Text(state.errors["name"] ?: "", color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = state.email,
                onValueChange = { vm.update("email", it) },
                label = { Text("Correo DUOC (@duoc.cl)") },
                isError = state.errors["email"] != null,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            AnimatedVisibility(state.errors["email"] != null, enter = fadeIn(), exit = fadeOut()) {
                Text(state.errors["email"] ?: "", color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = state.phone,
                onValueChange = { vm.update("phone", it) },
                label = { Text("Teléfono (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { vm.update("password", it) },
                label = { Text("Contraseña") },
                isError = state.errors["password"] != null,
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(state.errors["password"] != null, enter = fadeIn(), exit = fadeOut()) {
                Text(state.errors["password"] ?: "", color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = state.confirm,
                onValueChange = { vm.update("confirm", it) },
                label = { Text("Confirmar Contraseña") },
                isError = state.errors["confirm"] != null,
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(state.errors["confirm"] != null, enter = fadeIn(), exit = fadeOut()) {
                Text(state.errors["confirm"] ?: "", color = MaterialTheme.colorScheme.error)
            }

            val allGenres = listOf("Ficción","No Ficción","Misterio","Terror","Suspenso","Historia")
            Text("Géneros favoritos")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(Modifier.weight(1f)) {
                    allGenres.take(3).forEach { g ->
                        FilterChip(selected = state.genres.contains(g), onClick = { vm.toggleGenre(g) }, label = { Text(g) })
                    }
                }
                Column(Modifier.weight(1f)) {
                    allGenres.drop(3).forEach { g ->
                        FilterChip(selected = state.genres.contains(g), onClick = { vm.toggleGenre(g) }, label = { Text(g) })
                    }
                }
            }
            AnimatedVisibility(state.errors["genres"] != null, enter = fadeIn(), exit = fadeOut()) {
                Text(state.errors["genres"] ?: "", color = MaterialTheme.colorScheme.error)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = ::openGallery) { Text(if (state.avatarUri == null) "Elegir avatar" else "Cambiar avatar") }
            }

            Button(
                onClick = { vm.register(onRegistered) },
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (state.loading) "Registrando..." else "Crear cuenta") }
        }
    }
}
