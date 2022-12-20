package com.andrewwilkinson.blackjack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.andrewwilkinson.blackjack.ui.navigation.RootNavigation
import com.andrewwilkinson.blackjack.ui.theme.BlackJackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlackJackTheme {
                RootNavigation()
            }
        }
    }
}
