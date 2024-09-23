package com.example.summer_app.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.summer_app.data.ProfessorRatingData
import com.example.summer_app.screens.MainScreen.Companion.APP_DEFAULT_FONT
import com.example.summer_app.ui.theme.RatingGreen
import com.example.summer_app.ui.theme.RatingRed
import com.example.summer_app.ui.theme.RatingYellow
import com.example.summer_app.ui.theme.lightGray
import com.example.summer_app.ui.theme.scheduleGray
import com.example.summer_app.ui.theme.textGray


@Composable
fun ProfessorRatingDisplay(ratingData: ProfessorRatingData = ProfessorRatingData(), dataReady: Boolean = false){

    Box(contentAlignment = Alignment.CenterStart,
        modifier = Modifier.padding(start = 5.dp, end = 5.dp).background(lightGray, shape = RoundedCornerShape(14.dp)).padding(5.dp)
            .wrapContentHeight()
            .animateContentSize()){

        if (dataReady){
            Box(Modifier
                .padding(start = 5.dp, end = 5.dp)
                .background(lightGray, shape = RoundedCornerShape(14.dp)).padding(5.dp)
                .wrapContentHeight()
                .animateContentSize()
                , contentAlignment = Alignment.CenterStart){
                if (ratingData.review_num == 0) {
                    Text("This professor has no reviews yet", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier, color = textGray)
                } else {
                    ratingBallsIndicator(ratingData.overall_rating, ratingData.difficulty, ratingData.would_take_again, true)
                }
            }
        } else {
            ratingBallsIndicator()
        }
    }
}

@Composable
fun ratingBallsIndicator(rating: Double = 0.0, difficulty: Double = 0.0, recommend: Double = 0.0 , dataReady: Boolean = false) {

    Row(modifier = Modifier) {

        ballWithRating("Difficulty", difficulty, dataReady, isDifficulty = true)
        ballWithPercentage("Recommend", recommend, dataReady)
        ballWithRating("Rating", rating, dataReady)
    }
}

@Composable
fun ballWithRating(
    text: String,
    rating: Double = 0.0,
    dataReady: Boolean = false,
    isDifficulty: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val boxColor = when {
            rating > 4.0 -> if (isDifficulty) RatingRed else RatingGreen
            rating > 3.0 -> if (isDifficulty) RatingYellow else RatingYellow
            rating > 0.0 -> if (isDifficulty) RatingGreen else RatingRed
            else -> scheduleGray
        }

        Box(
            modifier = Modifier
                .padding(start = 5.dp, end = 3.dp)
                .width(16.dp)
                .height(16.dp)
                .background(color = boxColor, shape = RoundedCornerShape(10.dp))
        )
        Text(
            text = if (dataReady) "$text: $rating" else "",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = APP_DEFAULT_FONT,
        )
    }
}

@Composable
fun ballWithPercentage(text: String, rating: Double = 0.0, dataReady: Boolean = false) {
    Row( verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 5.dp, end = 3.dp)
                .width(16.dp)
                .height(16.dp)
                .background(
                    color = when {
                        rating >= 80.0 -> RatingGreen
                        rating >= 60.0 -> RatingYellow
                        rating > 0.0 -> RatingRed
                        else -> scheduleGray
                    },
                    shape = RoundedCornerShape(10.dp)
                )
        )
        // Display percentage when data is ready
        Text(
            text = if (dataReady) "$text: ${rating.toInt()}%" else if (rating.toInt() == -1) "N/A" else "",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = APP_DEFAULT_FONT,
        )
    }
}