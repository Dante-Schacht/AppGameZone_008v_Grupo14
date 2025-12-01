package com.example.appgamezone_008v_grupo14.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgamezone_008v_grupo14.GameZoneApplication
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.ui.components.GameZoneTopAppBar
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreen(currentUser: User?, onHomeClick: () -> Unit, onProfileClick: () -> Unit, onPedidosClick: () -> Unit, onAdminClick: () -> Unit, onCartClick: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val application = context.applicationContext as GameZoneApplication
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()
    var selectedItem by remember { mutableStateOf("pedidos") }

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
            topBar = {
                GameZoneTopAppBar(scope = scope, drawerState = drawerState, cartItemCount = cartItemCount, onCartClick = onCartClick)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Aquí se mostrará el historial de pedidos.")
            }
        }
    }
}