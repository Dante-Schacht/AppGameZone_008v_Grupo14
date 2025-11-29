package com.example.appgamezone_008v_grupo14.ui.screens.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appgamezone_008v_grupo14.ui.components.AppTopBar
import com.example.appgamezone_008v_grupo14.ui.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalPermissionsApi::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    val vm: ProfileViewModel = viewModel()
    val st = vm.state.collectAsState().value
    val context = LocalContext.current

    // --- Permisos y Launchers ---
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val galleryPermission = if (android.os.Build.VERSION.SDK_INT >= 33) rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES) else rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? -> vm.updateAvatar(uri?.toString()) }
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
        if (success && uri != null) vm.updateAvatar(uri.toString())
    }
    val askCameraPermission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            val uri = newCameraUri(context)
            cameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    Scaffold(topBar = { AppTopBar("Perfil", onBack) }) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!st.avatarUri.isNullOrBlank()) {
                    AsyncImage(
                        model = st.avatarUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Sin foto",
                        modifier = Modifier.size(96.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Nombre: ${st.fullName}")
                    Text("Email: ${st.email}")
                    if (st.phone != null) Text("Teléfono: ${st.phone}")
                }
            }

            // ----- Acciones: Galería / Cámara -----
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        if (galleryPermission.status.isGranted) galleryLauncher.launch("image/*") else galleryPermission.launchPermissionRequest()
                    }
                ) {
                    Icon(Icons.Filled.Photo, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Galería")
                }

                OutlinedButton(
                    onClick = {
                        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        if (granted) {
                            val uri = newCameraUri(context)
                            cameraUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            askCameraPermission.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Cámara")
                }
            }

            // --- Géneros Editables con FlowRow ---
            Text("Mis Géneros Favoritos")
            val allGenres = listOf("Ficción", "No Ficción", "Misterio", "Terror", "Suspenso", "Historia")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                allGenres.forEach { g ->
                    AnimatedFilterChip(label = g, selected = st.genres.contains(g), onClick = { vm.toggleGenre(g) })
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = { vm.logout(onLogout) }) { Text("Cerrar sesión") }
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
