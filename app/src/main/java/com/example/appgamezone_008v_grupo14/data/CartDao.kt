package com.example.appgamezone_008v_grupo14.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE gameId = :gameId")
    suspend fun deleteCartItem(gameId: Int)

    @Query("SELECT * FROM cart_items WHERE gameId = :gameId LIMIT 1")
    suspend fun getCartItemById(gameId: Int): CartItem?
}