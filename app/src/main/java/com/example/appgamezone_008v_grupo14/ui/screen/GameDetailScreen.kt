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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.appgamezone_008v_grupo14.GameZoneApplication
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModelFactory
import com.example.appgamezone_008v_grupo14.viewModels.GameUiState
import com.example.appgamezone_008v_grupo14.viewModels.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: Int,
    gameViewModel: GameViewModel,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val uiState by gameViewModel.selectedGameState.collectAsState()
    val context = LocalContext.current
    val application = context.applicationContext as GameZoneApplication
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()

    LaunchedEffect(gameId) {
        gameViewModel.fetchGameById(gameId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Juego") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge { Text(cartItemCount.toString()) }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito de compras"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (val state = uiState) {
                is GameUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is GameUiState.Success -> {
                    val game = state.games.firstOrNull()
                    if (game != null) {
                        var quantity by remember { mutableStateOf(1) }
                        val price = remember(game.id) { (20000..70000).random() }

                        Column(modifier = Modifier.fillMaxSize()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(game.thumbnail),
                                    contentDescription = game.title,
                                    modifier = Modifier
                                        .height(250.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = game.title, style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "$${price}", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Descripción", style = MaterialTheme.typography.titleLarge)
                            Text(game.shortDescription)
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Restar")
                                }
                                Text("$quantity", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
                                IconButton(onClick = { quantity++ }) {
                                    Icon(Icons.Default.Add, contentDescription = "Sumar")
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { 
                                val gameToAdd = com.example.appgamezone_008v_grupo14.network.Game(
                                    id = game.id,
                                    title = game.title,
                                    thumbnail = game.thumbnail,
                                    shortDescription = game.shortDescription,
                                    genre = game.genre,
                                    platform = game.platform,
                                    publisher = game.publisher,
                                    developer = game.developer,
                                    releaseDate = game.releaseDate
                                )
                                cartViewModel.addToCart(gameToAdd, quantity)
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Añadir al Carrito")
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