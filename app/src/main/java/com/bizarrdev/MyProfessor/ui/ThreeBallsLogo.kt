package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bizarrdev.MyProfessor.ui.theme.RatingGreen
import com.bizarrdev.MyProfessor.ui.theme.RatingRed
import com.bizarrdev.MyProfessor.ui.theme.RatingYellow

@Composable
fun ThreeBallsLogo() {
    Row {
        RatingBall(RatingGreen, paddingValues = 3.dp)
        RatingBall(RatingYellow, paddingValues = 3.dp)
        RatingBall(RatingRed, paddingValues = 3.dp)
    }
}