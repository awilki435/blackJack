package com.andrewwilkinson.blackjack.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.andrewwilkinson.blackjack.ui.repositories.UserRepository

class SplashScreenViewModel(application: Application): AndroidViewModel(application){
    fun isUserLoggedIn() = UserRepository.isUserLoggedIn()
}