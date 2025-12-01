package com.example.appgamezone_008v_grupo14.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgamezone_008v_grupo14.data.User
import com.example.appgamezone_008v_grupo14.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(private val userRepository: UserRepository) : ViewModel() {

    val allUsers: StateFlow<List<User>> = userRepository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateUserRole(user: User, newRole: String) {
        viewModelScope.launch {
            val updatedUser = user.copy(role = newRole)
            userRepository.updateUser(updatedUser)
        }
    }
}