package com.example.appgamezone_008v_grupo14

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.appgamezone_008v_grupo14.data.UserRepository
import com.example.appgamezone_008v_grupo14.ui.theme.Appgamezone_008v_grupo14Theme
import com.example.appgamezone_008v_grupo14.views.HomeScreen
import com.example.appgamezone_008v_grupo14.views.LoginScreen
import com.example.appgamezone_008v_grupo14.views.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ⚠️ AJUSTA el nombre del Theme a tu proyecto real (revisa /ui/theme/*)
            Appgamezone_008v_grupo14Theme {
                val repo = remember { UserRepository(this) }
                var currentScreen by remember {
                    mutableStateOf(if (repo.getSavedUser() != null) "home" else "login")
                }

                when (currentScreen) {
                    "login" -> LoginScreen(
                        onLoginSuccess = { currentScreen = "home" },
                        onGoToRegister = { currentScreen = "register" }
                    )
                    "register" -> RegisterScreen(
                        onGoToLogin = { currentScreen = "login" }
                    )
                    "home" -> HomeScreen(
                        onLogout = { currentScreen = "login" }
                    )
                }
            }
        }
    }
}
