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
fun RatingBall(color: Color, size: Dp, paddingValues: Dp){
    Box(
        modifier = Modifier
            .padding(horizontal = paddingValues)
            .width(size)
            .height(size)
            .background(color = color, shape = RoundedCornerShape(size/2))
    )
}