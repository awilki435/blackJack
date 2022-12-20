package com.andrewwilkinson.blackjack.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.andrewwilkinson.blackjack.ui.components.TextInput
import com.andrewwilkinson.blackjack.ui.viewmodels.GameViewModel
import com.andrewwilkinson.blackjack.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.async
import androidx.compose.animation.core.*
import androidx.compose.animation.slideIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import com.andrewwilkinson.blackjack.R
import com.andrewwilkinson.blackjack.ui.components.Loader
import kotlinx.coroutines.delay

@Composable
fun GameScreen(navController: NavController) {
    val viewModel: GameViewModel = viewModel()
    val state = viewModel.uiState

    LaunchedEffect(true) {
        state.loading = true
        val dealingCards = async { viewModel.dealCards() }
        delay(3000)
        dealingCards.await()
        state.sumUser = viewModel.sumUser()
        state.sumAi = viewModel.sumAi()
        state.loading = false
    }
    LaunchedEffect(state.bet) {
        val startGame = async { viewModel.startGame() }
        startGame.await()
    }
    LaunchedEffect(state.redeal) {
        state.redeal = false
        val startGame = async { viewModel.startGame() }
        startGame.await()
    }
    LaunchedEffect(state.userDone) {
        val startGame = async { viewModel.startGame() }
        startGame.await()
    }
    LaunchedEffect(state.aiDone) {
        val startGame = async { viewModel.startGame() }
        startGame.await()
    }
    if (state.loading) {
        Spacer(modifier = Modifier.size(16.dp))
        Loader()
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(text = "AI")
                Spacer(modifier = Modifier.size(24.dp))
                if (state.showing) {
                    Text(text = "Sum: ${state.sumAi}")
                }
            }
            Row {
                if (!state.showing) {
                    Card() {
                        Image(
                            painter = painterResource(id = R.drawable.cardback),
                            contentDescription = "Card Back",
                        )
                    }
                    Spacer(modifier = Modifier.size(24.dp))
                    Card() {
                        Image(
                            painter = painterResource(id = R.drawable.cardback),
                            contentDescription = "Card Back"
                        )
                    }
                }
                if (state.showing) {
                    var incrementY = 0f
                    for (i in 0 until state.aiCards.size) {
                        Card() {
                            Image(
                                painter = painterResource(id = state.getAICard(i).image!!),
                                contentDescription = state.getAICard(i).front
                            )
                        }
                        Spacer(modifier = Modifier.size(24.dp))
                        incrementY += 20f

                    }
                }
            }
            Spacer(modifier = Modifier.size(32.dp))
            Image(
                painter = painterResource(id = R.drawable.cardback),
                contentDescription = "card back"
            )
            Spacer(modifier = Modifier.size(32.dp))
            Row {
                Text(text = "User")
                Spacer(modifier = Modifier.size(24.dp))
                Text(text = "Sum: ${state.sumUser}")
            }
            Row {
                var incrementX = 0f
                for (item in state.userCards) {
                    Card(modifier = Modifier.graphicsLayer {
                        translationX = incrementX
                        translationY = 50f
                    }) {
                        Image(
                            painter = painterResource(id = item.image!!),
                            contentDescription = item.front
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    incrementX += 20f
                }
            }
            Row {
                if (!state.showing) {
                    Button(onClick = { viewModel.betUpdate() }) {
                        Text("Bet")
                    }
                }
            }
            if (state.showing) {
                Row {
                    Button(onClick = {
                        if (state.sumUser <= 21) {
                            viewModel.addUser()
                        } else {
                            state.userDone = true
                        }
                    })
                    {
                        Text("Add Card")
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = { state.userDone = true }) {
                        Text("Hold")
                    }
                }
            }
            if (state.win) {
                Text("Winner!")
            }
            if (state.lose) {
                Text("Loser!")
            }
            if (state.tie) {
                Text("Push!")
            }
        }
    }
}











