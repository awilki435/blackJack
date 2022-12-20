package com.andrewwilkinson.blackjack.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.andrewwilkinson.blackjack.R

@Composable
fun Loader(){
    val transition = rememberInfiniteTransition()
    val sizePercentage by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(
                durationMillis = 1000
            )
        )
    )

    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Surface(
            modifier = Modifier.size(
                ((300 * sizePercentage) + 100).dp
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.loader),
                contentDescription = "Loader",
            )
        }
    }
}
