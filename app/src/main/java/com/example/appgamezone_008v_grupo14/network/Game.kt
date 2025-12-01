package com.example.appgamezone_008v_grupo14.network

import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int,
    val title: String,
    val thumbnail: String,
    @SerializedName("short_description")
    val shortDescription: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    @SerializedName("release_date")
    val releaseDate: String
)
