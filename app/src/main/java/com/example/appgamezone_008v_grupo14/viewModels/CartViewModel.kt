package com.example.appgamezone_008v_grupo14.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgamezone_008v_grupo14.data.CartItem
import com.example.appgamezone_008v_grupo14.data.CartRepository
import com.example.appgamezone_008v_grupo14.network.Game
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.allCartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItemCount: StateFlow<Int> = cartRepository.allCartItems
        .map { items -> items.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToCart(game: Game, quantity: Int) {
        viewModelScope.launch {
            cartRepository.addToCart(game, quantity)
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItem)
        }
    }
}