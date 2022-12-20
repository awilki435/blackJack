package com.andrewwilkinson.blackjack.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.andrewwilkinson.blackjack.ui.repositories.SignUpException
import com.andrewwilkinson.blackjack.ui.repositories.UserRepository

class SignUpScreenState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordConfirmation by mutableStateOf("")
    var emailConfirmation by mutableStateOf("")
    var emailError by mutableStateOf(false)
    var emailConfirmationError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var passwordConfirmationError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var signUpSuccess by mutableStateOf(false)
    var name by mutableStateOf("")
}

class SignUpViewModel(application: Application): AndroidViewModel(application){
    val uiState = SignUpScreenState()

    suspend fun signUp(){
        uiState.emailError = false
        uiState.emailConfirmationError= false
        uiState.passwordError = false
        uiState.passwordConfirmationError = false
        uiState.errorMessage = ""
        if (!uiState.email.contains("@")){
            uiState.emailError = true
            uiState.errorMessage = "Email is invalid"
            return
        }
        if(uiState.email != uiState.emailConfirmation){
            uiState.emailConfirmationError = true
            uiState.errorMessage = "Emails do not match"
            return
        }
        if (uiState.password.length < 8){
            uiState.passwordError = true
            uiState.errorMessage = "Password needs to be at least 9 characters long"
            return
        }
        if(uiState.password != uiState.passwordConfirmation){
            uiState.passwordConfirmationError = true
            uiState.errorMessage = "Passwords do not match"
            return
        }
        try {
            UserRepository.createUser(uiState.email, uiState.password, name = uiState.name, money = 50000)
            uiState.signUpSuccess = true
        } catch(e: SignUpException){
            uiState.errorMessage = e.message ?: "Something went wrong. Please try again."
        }
    }
}