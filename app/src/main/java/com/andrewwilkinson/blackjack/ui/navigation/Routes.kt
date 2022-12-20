package com.andrewwilkinson.blackjack.ui.navigation

data class Screen(val route: String)

object Routes{
    val foyer = Screen("foyer")
    val signIn = Screen("signIn")
    val signUp = Screen("signUp")
    val home = Screen("home")
    val splashScreen = Screen("splash")
    val store = Screen("store")
    val game = Screen("game")

}