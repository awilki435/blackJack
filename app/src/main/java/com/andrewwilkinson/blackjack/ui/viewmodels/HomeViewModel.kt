package com.andrewwilkinson.blackjack.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.andrewwilkinson.blackjack.ui.repositories.UserRepository

class HomeState {
    var userCoins by mutableStateOf(0)
    var name by mutableStateOf("")
    var money by mutableStateOf(0)
    var loading by mutableStateOf(true)
    var bet by mutableStateOf(300)
    var incrementing by mutableStateOf(true)
}


class HomeViewModel(application: Application): AndroidViewModel(application) {
    val uiState = HomeState()

    fun getUserCoins(){
        uiState.userCoins = UserRepository.userCoins()
    }
    suspend fun getUser() {
        UserRepository.setUserCache()
        UserRepository.getCurrentUser().let { user ->
            uiState.name = user.name ?: ""
            uiState.money = user.money?: 0
        }
    }
    fun incrementing(value: Int){
        uiState.bet += value
    }
}
