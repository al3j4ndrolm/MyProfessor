package com.example.MyProfessor.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.MyProfessor.ui.theme.RatingGreen
import com.example.MyProfessor.ui.theme.RatingRed
import com.example.MyProfessor.ui.theme.RatingYellow

@Composable
fun ThreeBallsLogo() {
    Row {
        RatingBall(RatingGreen, paddingValues = 3.dp)
        RatingBall(RatingYellow, paddingValues = 3.dp)
        RatingBall(RatingRed, paddingValues = 3.dp)
    }
}