package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBall(color: Color, paddingValues: Dp = 4.dp){
    Box(
        modifier = Modifier
            .padding(start = paddingValues, end = paddingValues)
            .width(16.dp)
            .height(16.dp)
            .background(color = color, shape = RoundedCornerShape(10.dp))
    )
}