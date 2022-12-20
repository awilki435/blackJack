package com.andrewwilkinson.blackjack.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andrewwilkinson.blackjack.ui.models.StoreItem

@Composable
fun StoreItem(
    storeItem: StoreItem
){
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(12.dp)
            .fillMaxSize()

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$${storeItem.coinCount}")
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = { /*TODO*/ })
            {
                Text(text = "Price: $${storeItem.price}")
            }
        }
    }
}