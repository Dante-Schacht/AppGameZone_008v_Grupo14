package com.example.appgamezone_008v_grupo14

import android.app.Application
import com.example.appgamezone_008v_grupo14.data.AppDatabase
import com.example.appgamezone_008v_grupo14.data.CartRepository
import com.example.appgamezone_008v_grupo14.data.UserRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GameZoneApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val userRepository: UserRepository by lazy { UserRepository(database.userDao()) }
    val cartRepository: CartRepository by lazy { CartRepository(database.cartDao()) }
}