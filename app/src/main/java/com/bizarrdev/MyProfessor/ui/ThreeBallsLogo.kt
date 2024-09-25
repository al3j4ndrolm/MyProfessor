package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bizarrdev.MyProfessor.ui.theme.RatingGreen
import com.bizarrdev.MyProfessor.ui.theme.RatingRed
import com.bizarrdev.MyProfessor.ui.theme.RatingYellow

@Composable
fun ThreeBallsLogo(size: Dp, paddingValues: Dp) {
    Row {
        RatingBall(RatingGreen, size, paddingValues)
        RatingBall(RatingYellow, size, paddingValues)
        RatingBall(RatingRed, size, paddingValues)
    }
}