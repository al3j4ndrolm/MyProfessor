package com.example.summer_app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.summer_app.data.ProfessorRatingData
import com.example.summer_app.screens.RatingDisplay

@Composable
fun ProfessorRatingLoading(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.width(300.dp)
    ) {
        Text(
            text = "Fetching professor ratings...",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )
    }
}

@Composable
fun ProfessorRatingDisplay(ratingData: ProfessorRatingData){
    if (ratingData!!.review_num == 0) {
        Text("No ratings yet")
    } else {
        RatingDisplay(
            "Difficulty", ratingData!!.difficulty,
            isPercentage = false,
            isDifficulty = true
        )
        RatingDisplay(
            text = "Recommend",
            ratingData!!.would_take_again,
            isPercentage = true,
            isDifficulty = false
        )
        RatingDisplay(
            text = "Rating",
            ratingData!!.overall_rating,
            isPercentage = false,
            isDifficulty = false
        )
    }
}