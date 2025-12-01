package com.example.appgamezone_008v_grupo14.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImagenInteligente(imageUri: String?) {
    val imageSize = 150.dp

    Card(
        modifier = Modifier.size(imageSize),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = Uri.parse(imageUri)),
                contentDescription = "Imagen de perfil",
                modifier = Modifier.size(imageSize).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "Ícono de perfil por defecto",
                modifier = Modifier.size(imageSize)
            )
        }
    }
}