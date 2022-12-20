package com.andrewwilkinson.blackjack.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.andrewwilkinson.blackjack.ui.screens.*
import com.andrewwilkinson.blackjack.ui.viewmodels.RootNavigationViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RootNavigation(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewModel: RootNavigationViewModel = viewModel()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (currentDestination?.hierarchy?.none { it.route == Routes.foyer.route || it.route == Routes.splashScreen.route } == true) {
                TopAppBar() {
                    if (currentDestination?.route == Routes.home.route) {
                        IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu button")
                        }
                    } else {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                    Text(text = "Black Jack")
                }
            }
        },
        drawerContent = {
            if (currentDestination?.hierarchy?.none { it.route == Routes.foyer.route || it.route == Routes.splashScreen.route } == true) {
                DropdownMenuItem(onClick = {
                    scope.launch{
                        scaffoldState.drawerState.close()
                    }
                    navController.navigate(Routes.store.route)
                }) {
                    Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Store")
                }
                DropdownMenuItem(onClick = {
                    viewModel.signOutUser()
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    navController.navigate(Routes.signIn.route) {
                        popUpTo(0) // clear back stack and basically start the app from scratch
                    }
                }) {
                    Icon(Icons.Default.ExitToApp, "Logout")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Logout")
                }

            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.splashScreen.route,
            modifier = Modifier.padding(paddingValues = it)
        ) {
            composable(route = Routes.store.route){ StoreScreen(navController = navController) }
            composable(route = Routes.home.route){ HomeScreen(navController = navController)}
            composable(route = Routes.splashScreen.route) { SplashScreen(navController) }
            composable(route = Routes.signIn.route) { SignInScreen(navController = navController) }
            composable(route = Routes.signUp.route) { SignUpScreen(navController = navController) }
            composable(route = Routes.game.route) { GameScreen(navController = navController) }
        }
    }
}