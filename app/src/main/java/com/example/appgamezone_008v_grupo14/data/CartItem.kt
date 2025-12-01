package com.example.appgamezone_008v_grupo14.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val gameId: Int,
    val title: String,
    val thumbnail: String,
    var quantity: Int
)
