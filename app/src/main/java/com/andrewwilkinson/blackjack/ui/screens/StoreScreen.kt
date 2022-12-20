package com.andrewwilkinson.blackjack.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.andrewwilkinson.blackjack.ui.components.StoreItem
import com.andrewwilkinson.blackjack.ui.viewmodels.StoreViewModel
import kotlinx.coroutines.async

@Composable
fun StoreScreen(navController: NavController){
    val viewModel: StoreViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState
    LaunchedEffect(true){
        val loadingStore = async{viewModel.getStore()}
        loadingStore.await()
    }
    Column() {
        LazyColumn() {
            items(state.store){ storeItem->
                StoreItem(
                    storeItem = storeItem
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
