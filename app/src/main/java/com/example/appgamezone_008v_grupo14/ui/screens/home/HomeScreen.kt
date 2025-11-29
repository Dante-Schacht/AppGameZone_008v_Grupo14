package com.example.appgamezone_008v_grupo14.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appgamezone_008v_grupo14.ui.components.AppTopBar

data class Game(
    val id: String,
    val title: String,
    val coverUrl: String,
    val price: String,
    val genre: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(onGoProfile: () -> Unit) {
    // Demo data (puedes reemplazar por tu backend luego)
    val allGames = remember {
        listOf(
            Game("1","Elden Ring","https://picsum.photos/seed/elden/400/600","$39.990","RPG"),
            Game("2","Hades II","https://picsum.photos/seed/hades/400/600","$24.990","Roguelike"),
            Game("3","Forza Horizon 5","https://picsum.photos/seed/forza/400/600","$29.990","Carreras"),
            Game("4","Resident Evil 4","https://picsum.photos/seed/re4/400/600","$34.990","Terror"),
            Game("5","Zelda: TotK","https://picsum.photos/seed/zelda/400/600","$59.990","Aventura"),
            Game("6","Stardew Valley","https://picsum.photos/seed/stardew/400/600","$6.990","Simulación")
        )
    }
    val genres = remember { listOf("Todos","RPG","Roguelike","Carreras","Terror","Aventura","Simulación") }
    var selectedGenre by remember { mutableStateOf("Todos") }
    var query by remember { mutableStateOf("") }

    val filtered = remember(allGames, selectedGenre, query) {
        allGames.filter { g ->
            (selectedGenre == "Todos" || g.genre == selectedGenre) &&
            (query.isBlank() || g.title.contains(query, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "GameZone", onBack = null)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onGoProfile,
                icon = { Icon(Icons.Default.AccountCircle, null) },
                text = { Text("Mi perfil") }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Banner Promocional
            Card(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                AsyncImage(
                    model = "https://picsum.photos/seed/banner/800/200",
                    contentDescription = "Banner de oferta",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text("Bienvenido a GameZone", style = MaterialTheme.typography.headlineSmall)

            // Buscador
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Buscar juego...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Filtros (chips)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                genres.forEach { g ->
                    FilterChip(
                        selected = selectedGenre == g,
                        onClick = { selectedGenre = g },
                        label = { Text(g) }
                    )
                }
            }

            // Grid adaptable (responsive)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { game ->
                    GameCard(game = game)
                }
            }
        }
    }
}

@Composable
private fun GameCard(game: Game) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Portada
            AsyncImage(
                model = game.coverUrl,
                contentDescription = game.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
            Text(game.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(game.genre, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(game.price, style = MaterialTheme.typography.titleSmall)
                Button(onClick = { /* TODO: agregar al carrito */ }) {
                    Text("Agregar")
                }
            }
        }
    }
}
