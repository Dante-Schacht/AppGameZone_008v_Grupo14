package com.example.appgamezone_008v_grupo14.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgamezone_008v_grupo14.GameZoneApplication
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.ui.components.GameZoneTopAppBar
import com.example.appgamezone_008v_grupo14.viewModels.AuthViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModelFactory
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import com.example.appgamezone_008v_grupo14.viewModels.ProfileViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(currentUser: User?, authViewModel: AuthViewModel, onHomeClick: () -> Unit, onProfileClick: () -> Unit, onPedidosClick: () -> Unit, onAdminClick: () -> Unit, onCartClick: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val application = context.applicationContext as GameZoneApplication
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()
    var selectedItem by remember { mutableStateOf("perfil") }
    val profileViewModel: ProfileViewModel = viewModel()
    val imageUri by profileViewModel.imageUri.collectAsStateWithLifecycle()


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profileViewModel.onImageChange(uri)
        }
    )

    val file = context.createImageFile()
    val cameraUri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".fileprovider", file
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                profileViewModel.onImageChange(cameraUri)
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                cameraLauncher.launch(cameraUri)
            }
        }
    )


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedItem == "home",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onHomeClick()
                        }
                        selectedItem = "home"
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem == "perfil",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onProfileClick()
                        }
                        selectedItem = "perfil"
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") },
                    selected = selectedItem == "pedidos",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onPedidosClick()
                        }
                        selectedItem = "pedidos"
                    }
                )
                if (currentUser?.role == "admin") {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Administrar") },
                        label = { Text("Administrar") },
                        selected = selectedItem == "admin",
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                onAdminClick()
                            }
                            selectedItem = "admin"
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = { GameZoneTopAppBar(scope = scope, drawerState = drawerState, cartItemCount = cartItemCount, onCartClick = onCartClick) }
        ) { paddingValues ->
            val allGenres = listOf(
                "Acción",
                "Aventura",
                "RPG",
                "Estrategia",
                "Deportes",
                "Simulación",
                "Puzzle",
                "Terror"
            )

            var selectedGenres by remember(currentUser) {
                mutableStateOf(
                    currentUser?.genres?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        AsyncImage(model = imageUri, contentDescription = "profile photo")
                        Text(text = currentUser?.fullName ?: "Nombre de usuario")
                        Text(text = currentUser?.email ?: "email@duoc.cl")
                        Text(text = currentUser?.phone ?: "12312441")
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Abrir Galería")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    scope.launch {
                        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(cameraUri)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }) {
                    Text("Tomar Foto")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Mis géneros favoritos",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    allGenres.forEach { genre ->
                        ElevatedFilterChip(
                            selected = genre in selectedGenres,
                            onClick = {
                                selectedGenres = if (genre in selectedGenres) {
                                    selectedGenres - genre
                                } else {
                                    selectedGenres + genre
                                }
                            },
                            label = { Text(genre) }
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        currentUser?.let { user ->
                            val updatedUser = user.copy(
                                genres = selectedGenres.joinToString(",")
                            )
                            authViewModel.updateUser(updatedUser)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cambios")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { authViewModel.logout() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}