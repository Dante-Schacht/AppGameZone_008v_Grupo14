package com.example.appgamezone_008v_grupo14.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.appgamezone_008v_grupo14.GameZoneApplication
import com.example.appgamezone_008v_grupo14.R
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.ui.components.GameZoneTopAppBar
import com.example.appgamezone_008v_grupo14.viewModels.AuthViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModelFactory
import com.example.appgamezone_008v_grupo14.viewModels.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    currentUser: User?,
    authViewModel: AuthViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPedidosClick: () -> Unit,
    onAdminClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val application = context.applicationContext as GameZoneApplication

    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()
    var selectedItem by remember { mutableStateOf("profile") }

    val profileViewModel: ProfileViewModel = viewModel()
    val imageUri by profileViewModel.imageUri.collectAsState()
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { profileViewModel.onImageChange(it) } }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success -> if (success) tempCameraUri?.let { profileViewModel.onImageChange(it) } }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val newImageFile = context.createImageFile()
                val newImageUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newImageFile)
                tempCameraUri = newImageUri
                cameraLauncher.launch(newImageUri)
            }
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") }, selected = selectedItem == "home",
                    onClick = { scope.launch { drawerState.close() }; onHomeClick(); selectedItem = "home" })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil") }, selected = selectedItem == "profile",
                    onClick = { scope.launch { drawerState.close() }; onProfileClick(); selectedItem = "profile" })
                NavigationDrawerItem(icon = { Icon(Icons.Default.ShoppingBag, null) }, label = { Text("Pedidos") }, selected = selectedItem == "pedidos",
                    onClick = { scope.launch { drawerState.close() }; onPedidosClick(); selectedItem = "pedidos" })
                if (currentUser?.role == "admin") {
                    NavigationDrawerItem(icon = { Icon(Icons.Default.AdminPanelSettings, null) }, label = { Text("Administrar") }, selected = selectedItem == "admin",
                        onClick = { scope.launch { drawerState.close() }; onAdminClick(); selectedItem = "admin" })
                }
            }
        }
    ) {
        Scaffold(topBar = { GameZoneTopAppBar(scope, drawerState, cartItemCount, onCartClick) }) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(100.dp).clip(CircleShape),
                        placeholder = painterResource(R.drawable.gamezone_logo),
                        error = painterResource(R.drawable.gamezone_logo),
                        contentScale = ContentScale.Crop
                    )
                    Column {
                        Text("Nombre: ${currentUser?.fullName ?: "No disponible"}")
                        Text("Email: ${currentUser?.email ?: "No disponible"}")
                        Text("Teléfono: ${currentUser?.phone ?: "No disponible"}")
                    }
                }
                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { galleryLauncher.launch("image/*") }) { Text("Galería") }

                    Button(onClick = {
                        val permission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            val newImageFile = context.createImageFile()
                            val newImageUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newImageFile)
                            tempCameraUri = newImageUri
                            cameraLauncher.launch(newImageUri)
                        } else {
                            cameraPermissionLauncher.launch(permission)
                        }
                    }) { Text("Tomar Foto") }
                }

                Spacer(Modifier.height(32.dp))

                Text("Mis géneros favoritos", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                val allGenres = listOf("Acción", "Aventura", "RPG", "Estrategia", "Deportes", "Simulación", "Puzzle", "Terror")
                var selectedGenres by remember(currentUser) { mutableStateOf(currentUser?.genres?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()) }

                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    allGenres.forEach { genre ->
                        ElevatedFilterChip(
                            selected = genre in selectedGenres,
                            onClick = { selectedGenres = if (genre in selectedGenres) selectedGenres - genre else selectedGenres + genre },
                            label = { Text(genre) }
                        )
                    }
                }
                Spacer(Modifier.weight(1f))

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    currentUser?.let { authViewModel.updateUser(it.copy(genres = selectedGenres.joinToString(","))) }
                }) { Text("Guardar Cambios") }

                Spacer(Modifier.height(8.dp))
                Button(modifier = Modifier.fillMaxWidth(), onClick = { authViewModel.logout() }) { Text("Cerrar Sesión") }
            }
        }
    }
}

private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}
