package com.example.appgamezone_008v_grupo14.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appgamezone_008v_grupo14.ui.components.AppTopBar

@Composable
fun HomeScreen(onGoProfile: () -> Unit) {
    Scaffold(
        topBar = { AppTopBar(title = "Inicio") }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Bienvenido a GameZone")
            Button(onClick = onGoProfile) {
                Text("Ver mi perfil")
            }
        }
    }
}
