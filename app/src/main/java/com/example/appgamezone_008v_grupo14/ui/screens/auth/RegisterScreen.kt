package com.example.appgamezone_008v_grupo14.ui.screens.auth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgamezone_008v_grupo14.ui.components.AppTopBar
import com.example.appgamezone_008v_grupo14.ui.viewmodel.AuthViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalPermissionsApi::class, ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(onRegistered: () -> Unit, onBack: () -> Unit) {
    val vm: AuthViewModel = viewModel()
    val state by vm.state.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(state.loggedIn) {
        if (state.loggedIn) {
            onRegistered()
        }
    }

    // --- Permisos y Launchers ---
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val galleryPermission = if (android.os.Build.VERSION.SDK_INT >= 33) rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES) else rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { vm.setAvatar(it.toString()) }
    }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", context.externalCacheDir)
    }
    fun newCameraUri(context: Context): Uri {
        val file = createImageFile(context)
        return FileProvider.getUriForFile(context, "com.example.appgamezone_008v_grupo14.fileprovider", file)
    }
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success: Boolean ->
        val uri = cameraUri
        if (success && uri != null) vm.setAvatar(uri.toString())
    }
    val askCameraPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            val uri = newCameraUri(context)
            cameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cambiar foto de perfil") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        showDialog = false
                        if (galleryPermission.status.isGranted) galleryLauncher.launch("image/*") else galleryPermission.launchPermissionRequest()
                    }) {
                        Icon(Icons.Default.Photo, null, Modifier.padding(end = 4.dp))
                        Text("Galería")
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = {
                        showDialog = false
                        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        if (granted) {
                            val uri = newCameraUri(context)
                            cameraUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            askCameraPermission.launch(Manifest.permission.CAMERA)
                        }
                    }) {
                        Icon(Icons.Default.CameraAlt, null, Modifier.padding(end = 4.dp))
                        Text("Cámara")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(topBar = { AppTopBar("Registro", onBack) }) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = state.fullName, onValueChange = { vm.update("name", it) }, label = { Text("Nombre completo") }, isError = state.errors["name"] != null, modifier = Modifier.fillMaxWidth())
            AnimatedVisibility(state.errors["name"] != null, enter = fadeIn(), exit = fadeOut()) { Text(state.errors["name"] ?: "", color = MaterialTheme.colorScheme.error) }

            OutlinedTextField(value = state.email, onValueChange = { vm.update("email", it) }, label = { Text("Correo DUOC (@duoc.cl)") }, isError = state.errors["email"] != null, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            AnimatedVisibility(state.errors["email"] != null, enter = fadeIn(), exit = fadeOut()) { Text(state.errors["email"] ?: "", color = MaterialTheme.colorScheme.error) }

            OutlinedTextField(value = state.phone, onValueChange = { if (it.length <= 9) vm.update("phone", it) }, label = { Text("Teléfono (opcional)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))

            // --- Campo de Contraseña con visibilidad ---
            OutlinedTextField(
                value = state.password,
                onValueChange = { vm.update("password", it) },
                label = { Text("Contraseña") },
                isError = state.errors["password"] != null,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            AnimatedVisibility(state.errors["password"] != null, enter = fadeIn(), exit = fadeOut()) { Text(state.errors["password"] ?: "", color = MaterialTheme.colorScheme.error) }

            // --- Campo de Confirmar Contraseña con visibilidad ---
            OutlinedTextField(
                value = state.confirm,
                onValueChange = { vm.update("confirm", it) },
                label = { Text("Confirmar Contraseña") },
                isError = state.errors["confirm"] != null,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            AnimatedVisibility(state.errors["confirm"] != null, enter = fadeIn(), exit = fadeOut()) { Text(state.errors["confirm"] ?: "", color = MaterialTheme.colorScheme.error) }

            val allGenres = listOf("Ficción", "No Ficción", "Misterio", "Terror", "Suspenso", "Historia")
            Text("Géneros favoritos")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                allGenres.forEach { g ->
                    AnimatedFilterChip(label = g, selected = state.genres.contains(g), onClick = { vm.toggleGenre(g) })
                }
            }
            AnimatedVisibility(state.errors["genres"] != null, enter = fadeIn(), exit = fadeOut()) { Text(state.errors["genres"] ?: "", color = MaterialTheme.colorScheme.error) }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { showDialog = true }) { Text(if (state.avatarUri == null) "Elegir avatar" else "Cambiar avatar") }
            }
            Button(onClick = { vm.register() }, enabled = !state.loading, modifier = Modifier.fillMaxWidth()) { Text(if (state.loading) "Registrando..." else "Crear cuenta") }
        }
    }
}

@Composable
private fun AnimatedFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val borderColor by animateColorAsState(if (selected) MaterialTheme.colorScheme.primary else Color.Gray, label = "chip_border_color")
    val borderWidth by animateDpAsState(if (selected) 2.dp else 1.dp, label = "chip_border_width")

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        border = BorderStroke(borderWidth, borderColor)
    )
}
