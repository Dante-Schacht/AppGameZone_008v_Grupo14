package com.example.appgamezone_008v_grupo14;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appgamezone_008v_grupo14.ui.screens.auth.LoginScreen
import com.example.appgamezone_008v_grupo14.ui.screens.auth.RegisterScreen
import com.example.appgamezone_008v_grupo14.ui.screens.home.HomeScreen
import com.example.appgamezone_008v_grupo14.ui.screens.profile.ProfileScreen
import com.example.appgamezone_008v_grupo14.ui.theme.AppGameZone_008v_Grupo14Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppGameZone_008v_Grupo14Theme {
                AppNav()
            }
        }
    }
}

sealed class Routes(val route: String) {
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object Home : Routes("home")
    data object Profile : Routes("profile")
}

@Composable
fun AppNav(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Routes.Login.route,
        enterTransition = { fadeIn(animationSpec = tween(250)) },
        exitTransition = { fadeOut(animationSpec = tween(250)) }
    ) {
        composable(Routes.Register.route) {
            RegisterScreen(
                onRegistered = { navController.navigate(Routes.Home.route) { popUpTo(0) } },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Login.route) {
            LoginScreen(
                onLogged = { navController.navigate(Routes.Home.route) { popUpTo(0) } },
                onBack = { /* No back action */ },
                onGoRegister = { navController.navigate(Routes.Register.route) }
            )
        }
        composable(Routes.Home.route) {
            HomeScreen(onGoProfile = { navController.navigate(Routes.Profile.route) })
        }
        composable(Routes.Profile.route) {
            ProfileScreen(onBack = { navController.popBackStack() },
                onLogout = { navController.navigate(Routes.Login.route) { popUpTo(0) } })
        }
    }
}
