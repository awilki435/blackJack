package com.andrewwilkinson.blackjack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.andrewwilkinson.blackjack.R
import com.andrewwilkinson.blackjack.ui.models.User
import com.andrewwilkinson.blackjack.ui.navigation.Routes
import com.andrewwilkinson.blackjack.ui.repositories.UserRepository
import com.andrewwilkinson.blackjack.ui.viewmodels.HomeViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.*

@Composable
fun HomeScreen(navController: NavController){
    val viewModel: HomeViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState

    LaunchedEffect(true) {
        val loadingUser = async {viewModel.getUser()}
        loadingUser.await()
        state.loading = false
    }
    if (state.loading) {
        Spacer(modifier = Modifier.height(16.dp))
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Welcome to Black Jack!",
                style = MaterialTheme.typography.h5
            )
            Text("Balance: $${state.money}")
            Spacer(modifier = Modifier.size(16.dp))
            Image(painter = painterResource(id = R.drawable.cards),
                contentDescription = "Cards")
            Spacer(modifier = Modifier.size(16.dp))
            Row {
                Button(onClick = {viewModel.incrementing(-100)}) {
                    Text("-")
                }
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "Bet: $${state.bet}")
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = { viewModel.incrementing(100) }) {
                    Text("+")
                }

            }
            Spacer(modifier = Modifier.size(24.dp))
            Button(onClick = { navController.navigate(Routes.game.route) }) {
                Text("Play game")
            }
            //put add here
            //ca-app-pub-3940256099942544/6300978111
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    AdView(context).apply{
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}