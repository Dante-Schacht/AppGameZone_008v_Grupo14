package com.example.appgamezone_008v_grupo14.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appgamezone_008v_grupo14.GameZoneApplication
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.ui.components.GameZoneTopAppBar
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModelFactory
import com.example.appgamezone_008v_grupo14.viewModels.GameUiState
import com.example.appgamezone_008v_grupo14.viewModels.GameViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(currentUser: User?, onHomeClick: () -> Unit, onProfileClick: () -> Unit, onPedidosClick: () -> Unit, onAdminClick: () -> Unit, onCartClick: () -> Unit, onGameClick: (Int) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("home") }
    val gameViewModel: GameViewModel = viewModel()
    val context = LocalContext.current
    val application = context.applicationContext as GameZoneApplication
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val uiState by gameViewModel.uiState.collectAsState()
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()

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
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (val state = uiState) {
                    is GameUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is GameUiState.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.games) { game ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable { onGameClick(game.id) },
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Image(
                                            painter = rememberAsyncImagePainter(game.thumbnail),
                                            contentDescription = game.title,
                                            modifier = Modifier
                                                .height(120.dp)
                                                .fillMaxWidth(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = game.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                            Button(
                                                onClick = { cartViewModel.addToCart(game, 1) },
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("Añadir al carrito")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is GameUiState.Error -> {
                        Text(text = "Error: ${state.message}")
                    }
                }
            }
        }
    }
}