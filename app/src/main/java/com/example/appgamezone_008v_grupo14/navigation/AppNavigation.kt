package com.example.appgamezone_008v_grupo14.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appgamezone_008v_grupo14.GameZoneApplication
import com.example.appgamezone_008v_grupo14.ui.screen.AdminScreen
import com.example.appgamezone_008v_grupo14.ui.screen.CartScreen
import com.example.appgamezone_008v_grupo14.ui.screen.GameDetailScreen
import com.example.appgamezone_008v_grupo14.ui.screen.HomeScreen
import com.example.appgamezone_008v_grupo14.ui.screen.LoginScreen
import com.example.appgamezone_008v_grupo14.ui.screen.PedidosScreen
import com.example.appgamezone_008v_grupo14.ui.screen.ProfileScreen
import com.example.appgamezone_008v_grupo14.ui.screen.RegisterScreen
import com.example.appgamezone_008v_grupo14.viewModels.AuthViewModel
import com.example.appgamezone_008v_grupo14.viewModels.AuthViewModelFactory
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModel
import com.example.appgamezone_008v_grupo14.viewModels.CartViewModelFactory
import com.example.appgamezone_008v_grupo14.viewModels.GameViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as GameZoneApplication
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(application.userRepository))
    val gameViewModel: GameViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()

    val currentUser by authViewModel.currentUser.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentUser, currentRoute) {
        if (currentUser != null) {
            if (currentRoute == "login" || currentRoute == "register") {
                navController.navigate("home") {
                    popUpTo(currentRoute) { inclusive = true }
                }
            }
        } else {
            if (currentRoute != "login" && currentRoute != "register") {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login") { popUpTo("login") { inclusive = true } } }
            )
        }
        composable("home") {
            HomeScreen(
                currentUser = currentUser,
                onHomeClick = { navController.navigate("home") },
                onProfileClick = { navController.navigate("profile") },
                onPedidosClick = { navController.navigate("pedidos") },
                onAdminClick = { navController.navigate("admin") },
                onCartClick = { navController.navigate("cart") },
                onGameClick = { gameId -> navController.navigate("gameDetail/$gameId") }
            )
        }
        composable("profile") {
            ProfileScreen(
                currentUser = currentUser,
                authViewModel = authViewModel,
                onHomeClick = { navController.navigate("home") },
                onProfileClick = { navController.navigate("profile") },
                onPedidosClick = { navController.navigate("pedidos") },
                onAdminClick = { navController.navigate("admin") },
                onCartClick = { navController.navigate("cart") }
            )
        }
        composable("pedidos") {
            PedidosScreen(
                currentUser = currentUser,
                onHomeClick = { navController.navigate("home") },
                onProfileClick = { navController.navigate("profile") },
                onPedidosClick = { navController.navigate("pedidos") },
                onAdminClick = { navController.navigate("admin") },
                onCartClick = { navController.navigate("cart") }
            )
        }
        composable("admin") {
            AdminScreen(
                currentUser = currentUser,
                onHomeClick = { navController.navigate("home") },
                onProfileClick = { navController.navigate("profile") },
                onPedidosClick = { navController.navigate("pedidos") },
                onAdminClick = { navController.navigate("admin") },
                onCartClick = { navController.navigate("cart") }
            )
        }
        composable("cart") {
            CartScreen(
                currentUser = currentUser,
                onHomeClick = { navController.navigate("home") },
                onProfileClick = { navController.navigate("profile") },
                onPedidosClick = { navController.navigate("pedidos") },
                onAdminClick = { navController.navigate("admin") },
                onCartClick = { navController.navigate("cart") }
            )
        }
        composable(
            route = "gameDetail/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) {
            val gameId = it.arguments?.getInt("gameId") ?: 0
            GameDetailScreen(
                gameId = gameId,
                gameViewModel = gameViewModel,
                onBackClick = { navController.popBackStack() },
                onCartClick = { navController.navigate("cart") }
            )
        }
    }
}
