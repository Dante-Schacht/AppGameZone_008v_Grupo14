package com.example.appgamezone_008v_grupo14.data

import com.example.appgamezone_008v_grupo14.network.Game
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    val allCartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun addToCart(game: Game, quantity: Int) {
        val cartItem = cartDao.getCartItemById(game.id)
        if (cartItem == null) {
            cartDao.insertCartItem(CartItem(game.id, game.title, game.thumbnail, quantity))
        } else {
            val updatedItem = cartItem.copy(quantity = cartItem.quantity + quantity)
            cartDao.updateCartItem(updatedItem)
        }
    }

    suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem.gameId)
    }
}