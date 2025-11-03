package com.example.appgamezone_008v_grupo14.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.outlined.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appgamezone_008v_grupo14.data.UserRepository

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val repo = remember { UserRepository(context) }
    val user = remember { repo.getSavedUser() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )

        Text(
            text = "¡Bienvenido(a) a GAMEZONE!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        if (user != null) {
            Text("Nombre: ${user.fullName}", style = MaterialTheme.typography.titleMedium)
            Text("Correo: ${user.email}", style = MaterialTheme.typography.bodyMedium)
            if (user.genres.isNotEmpty()) {
                Text("Géneros favoritos:", style = MaterialTheme.typography.titleSmall)
                Text(user.genres.joinToString(" • "), style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Text(
                "No hay usuario almacenado en este dispositivo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { /* Navegar a otra sección si quieres */ },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Explorar") }

        OutlinedButton(
            onClick = {
                repo.clearUser()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cerrar sesión") }
    }
}
