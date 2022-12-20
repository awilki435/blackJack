package com.andrewwilkinson.blackjack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.andrewwilkinson.blackjack.ui.navigation.Routes
import com.andrewwilkinson.blackjack.ui.viewmodels.SplashScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){
    val viewModel: SplashScreenViewModel = viewModel()
    LaunchedEffect(true) {
        // wait for 3 seconds or until the login check is
        // done before navigating
        delay(1000)
        // TODO: if logged in the skip the launch
        //       flow and go straight to the application
        navController.navigate(
            if(viewModel.isUserLoggedIn()) {
                Routes.home.route
            }
            else{
                Routes.signIn.route
            }
        ) {
            // makes it so that we can't get back to the
            // splash screen by pushing the back button
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Black Jack",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center
        )
    }
}