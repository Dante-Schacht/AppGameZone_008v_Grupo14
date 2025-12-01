package com.example.appgamezone_008v_grupo14.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appgamezone_008v_grupo14.GameZoneApplication
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.ui.components.GameZoneTopAppBar
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(currentUser: User?, onHomeClick: () -> Unit, onProfileClick: () -> Unit, onPedidosClick: () -> Unit, onAdminClick: () -> Unit, onCartClick: () -> Unit) {
    val context = LocalContext.current
    val application = context.applicationContext as GameZoneApplication
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("cart") } // Default selected item

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedItem == "home",
                    onClick = { scope.launch { drawerState.close(); onHomeClick() }; selectedItem = "home" }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem == "perfil",
                    onClick = { scope.launch { drawerState.close(); onProfileClick() }; selectedItem = "perfil" }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") },
                    selected = selectedItem == "pedidos",
                    onClick = { scope.launch { drawerState.close(); onPedidosClick() }; selectedItem = "pedidos" }
                )
                if (currentUser?.role == "admin") {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Administrar") },
                        label = { Text("Administrar") },
                        selected = selectedItem == "admin",
                        onClick = { scope.launch { drawerState.close(); onAdminClick() }; selectedItem = "admin" }
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
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (cartItems.isEmpty()) {
                    Text("Tu carrito está vacío.")
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartItems) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(item.thumbnail),
                                        contentDescription = item.title,
                                        modifier = Modifier.size(80.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        Text(item.title)
                                        Text("Cantidad: ${item.quantity}")
                                    }
                                    IconButton(onClick = { cartViewModel.removeFromCart(item) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* TODO: Checkout logic */ }, modifier = Modifier.fillMaxWidth()) {
                        Text("Finalizar Compra")
                    }
                }
            }
        }
    }
}