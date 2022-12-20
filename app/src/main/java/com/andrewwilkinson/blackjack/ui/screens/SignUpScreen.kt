package com.andrewwilkinson.blackjack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.andrewwilkinson.blackjack.ui.components.SignButton
import com.andrewwilkinson.blackjack.ui.components.TextInput
import com.andrewwilkinson.blackjack.ui.navigation.Routes
import com.andrewwilkinson.blackjack.ui.viewmodels.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController){
    val viewModel: SignUpViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState
    LaunchedEffect(state.signUpSuccess){
        if(state.signUpSuccess){
            navController.navigate(Routes.signIn.route){
                popUpTo(0)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Create Account",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = state.errorMessage,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            TextInput(
                value = state.email,
                onValueChange = { state.email = it },
                placeholder = { Text("Email") },
                error = state.emailError
            )
            TextInput(
                value = state.emailConfirmation,
                onValueChange = { state.emailConfirmation = it },
                placeholder = { Text("Email Confirmation") },
                error = state.emailConfirmationError
            )
            TextInput(
                value = state.password,
                onValueChange = { state.password = it },
                placeholder = { Text("Password") },
                error = state.passwordError,
                password = true
            )
            TextInput(
                value = state.passwordConfirmation,
                onValueChange = { state.passwordConfirmation = it },
                placeholder = { Text("Password Confirmation") },
                error = state.passwordConfirmationError,
                password = true
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                SignButton(
                    text = "Create Account",
                    onClick = { scope.launch { viewModel.signUp() } })
            }
        }
    }
}