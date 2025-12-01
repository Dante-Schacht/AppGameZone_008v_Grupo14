package com.example.appgamezone_008v_grupo14.network

import retrofit2.http.GET
import retrofit2.http.Query

interface FreeToGameService {
    @GET("games")
    suspend fun getGames(): List<Game>

    @GET("game")
    suspend fun getGameById(@Query("id") id: Int): Game
}