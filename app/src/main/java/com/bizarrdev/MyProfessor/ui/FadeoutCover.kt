package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FadeoutCover(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp) // Height of the fading box at the top
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White, // Fade color
                        Color.White.copy(0.8f), // Fade color
                        Color.Transparent // Transparent bottom
                    )
                )
            )
    )
}