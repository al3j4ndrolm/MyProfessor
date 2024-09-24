package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.data.ProfessorRatingData
import com.bizarrdev.MyProfessor.screens.MainScreen.Companion.APP_DEFAULT_FONT
import com.bizarrdev.MyProfessor.ui.theme.NoRatingsTextColor
import com.bizarrdev.MyProfessor.ui.theme.RatingGray1
import com.bizarrdev.MyProfessor.ui.theme.RatingGray2
import com.bizarrdev.MyProfessor.ui.theme.RatingGray3
import com.bizarrdev.MyProfessor.ui.theme.RatingGreen
import com.bizarrdev.MyProfessor.ui.theme.RatingRed
import com.bizarrdev.MyProfessor.ui.theme.RatingTextColor
import com.bizarrdev.MyProfessor.ui.theme.RatingYellow


@Composable
fun RatingsSection(ratingData: ProfessorRatingData?) {
    RatingsSectionRowBackground{
        Row {
            if (ratingData != null) {
                ProfessorRatingDisplay(ratingData)
            } else {
                RatingStillFetchingDisplay()
            }
        }
    }
}

@Composable
fun ProfessorRatingDisplay(ratingData: ProfessorRatingData){
    if (ratingData.review_num == 0) {
        Text(
            text = "No reviews yet",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = APP_DEFAULT_FONT,
            color = NoRatingsTextColor,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    } else {
        RatingBallsAndTexts(
            ratingData.overall_rating,
            ratingData.difficulty,
            ratingData.would_take_again
        )
    }
}

@Composable
fun RatingStillFetchingDisplay(){
    Row(verticalAlignment = Alignment.CenterVertically) {
        RatingBall(color = RatingGray1)
        RatingBall(color = RatingGray2)
        RatingBall(color = RatingGray3)
    }
}

@Composable
fun RatingBallsAndTexts(rating: Double = 0.0, difficulty: Double = 0.0, recommend: Double = 0.0) {
    Row(modifier = Modifier) {
        RatingBallWithTextDouble("Difficulty", difficulty, isDifficulty = true)
        RatingBallWithTextPercentage("Recommend", recommend)
        RatingBallWithTextDouble("Rating", rating)
    }
}

@Composable
fun RatingBallWithTextDouble(
    text: String,
    rating: Double = 0.0,
    isDifficulty: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val ballColor = when {
            rating > 4.0 -> if (isDifficulty) RatingRed else RatingGreen
            rating > 3.0 -> if (isDifficulty) RatingYellow else RatingYellow
            else -> if (isDifficulty) RatingGreen else RatingRed
        }

        RatingBall(color = ballColor)
        RatingText(text = "$text: $rating", color = RatingTextColor)
    }
}

@Composable
fun RatingBallWithTextPercentage(text: String, rating: Double = 0.0) {
    Row( verticalAlignment = Alignment.CenterVertically
    ) {
        val ballColor = when {
            rating >= 80.0 -> RatingGreen
            rating >= 60.0 -> RatingYellow
            else -> RatingRed
        }

        RatingBall(color = ballColor)
        RatingText(
            text = if (rating.toInt() != -1) "$text: ${rating.toInt()}%" else  "N/A",
            color = RatingTextColor)
    }
}

@Composable
fun RatingText(text: String, color: Color){
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = APP_DEFAULT_FONT,
        color = color,
        modifier = Modifier.padding(end = 3.dp)
    )
}