package com.andrewwilkinson.blackjack.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.andrewwilkinson.blackjack.ui.repositories.UserRepository

class RootNavigationState {
}

class RootNavigationViewModel(application: Application): AndroidViewModel(application) {
    val uiState = RootNavigationState()
    fun signOutUser() {
        UserRepository.signOutUser()
    }
}