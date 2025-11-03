package com.example.appgamezone_008v_grupo14.data

data class User(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val genres: List<String>
)