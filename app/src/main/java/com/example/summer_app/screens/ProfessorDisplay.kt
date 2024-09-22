package com.example.summer_app.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.summer_app.data.Professor
import com.example.summer_app.data.ProfessorRatingData
import com.example.summer_app.ui.ProfessorRatingDisplay
import com.example.summer_app.ui.ProfessorRatingLoading
import com.example.summer_app.ui.theme.blue
import com.example.summer_app.ui.theme.RatingGreen
import com.example.summer_app.ui.theme.lightGray
import com.example.summer_app.ui.theme.RatingYellow
import com.example.summer_app.ui.theme.RatingRed
import com.example.summer_app.ui.theme.scheduleGray
import com.example.summer_app.usecase.DataManager

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ProfessorCard(
    dataManager: DataManager,
    professor: Professor,
    department: String
) {
    val context = LocalContext.current
    var ratingData: ProfessorRatingData? by remember { mutableStateOf(professor.ratingData) }

    LaunchedEffect(Unit) {
        dataManager.fetchProfessorRatings(
            context = context,
            professorName = professor.name,
            department = department,
            onResultReceived = {
                professor.ratingData = it
                ratingData = it
            })
    }

    Box(
        modifier = Modifier
            .padding(start = 10.dp, end = 8.dp, top = 5.dp, bottom = 8.dp)
            .background(color = lightGray, shape = RoundedCornerShape(16.dp))
            .wrapContentHeight()
            .animateContentSize()
    ) {
        Box {
            Column(Modifier.padding(start = 6.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(6.dp)
                ) {
                    Column(Modifier) {
                        ProfessorNameAndReviewsCount(
                            professorName = professor.name,
                            ratingData = ratingData,
                        )

                        RatingsSection(ratingData = ratingData)

                        ScheduleSection(allSchedules = professor.all_schedules)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfessorNameAndReviewsCount(professorName: String, ratingData: ProfessorRatingData?){
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(professorName)
            }
            if (ratingData != null) {
                append(" ")
                withStyle(
                    style = SpanStyle(
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("${ratingData.review_num} reviews")
                }
            }
        },
        textAlign = TextAlign.Start
    )
}

@Composable
fun RatingsSection(ratingData: ProfessorRatingData?){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (ratingData != null) {
            ProfessorRatingDisplay(ratingData)
        } else {
            ProfessorRatingLoading()
        }
    }
}

@Composable
fun ScheduleSection(allSchedules: Map<String, List<String>>) {
    var showSchedule by remember { mutableStateOf(false) }

    Divider(
        Color.Black,
        startText = "Schedules",
        isClickable = true,
        onClick = { showSchedule = !showSchedule })

    if (showSchedule) {
        for ((code, schedules) in allSchedules) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .background(scheduleGray, RoundedCornerShape(12.dp))
                ) {
                    Column {
                        for (schedule in schedules) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Text(
                                    text = schedule.substringBefore("/"),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(5.dp)
                                )
                                DisplayTag(
                                    schedule,
                                    isClassRoom = true,
                                    color = blue,
                                    textColor = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Divider(
    color: Color,
    startText: String = "",
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        if (isClickable) {
            Text(startText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 0.dp)
                    .clickable { onClick() }
            )
        } else {
            Text(
                startText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 0.dp)
            )
        }

        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .width(220.dp)
                .padding(start = 3.dp, top = 8.dp, bottom = 8.dp),
            color = color
        )
    }
}

@Composable
fun DisplayTag(value: String, isClassRoom: Boolean = false, color: Color, textColor: Color) {
    var text = value

    if (isClassRoom) {
        text = value.substringAfter("/")
    }

    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(10.dp))
            .padding(start = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text, fontSize = 12.sp,
            modifier = Modifier
                .padding(top = 2.dp, bottom = 2.dp, start = 8.dp, end = 8.dp),
            color = textColor, fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.width(4.dp))
}

@Composable
fun RatingDisplay(text: String, value: Double, isPercentage: Boolean, isDifficulty: Boolean) {
    if (isPercentage) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            PercentageCircle(value)
            Text(
                "${text}: ${value.toInt()} %",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            ValueCircle(value, isDifficulty)
            Text(
                "${text}: $value",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }
    }
}


@Composable
fun RatingDot(value: Double, color: Pair<IntRange, Color>) {

    val rangeToColor = listOf(
        0..10 to RatingRed,
        11..20 to RatingYellow,
        21..30 to RatingGreen,
    )

}


@Composable
fun ValueCircle(value: Double, isDifficulty: Boolean) {
    val color: Color = if (value > 4.0) {
        if (isDifficulty) {
            RatingRed
        } else {
            RatingGreen
        }
    } else if (value > 3.0) {
        RatingYellow
    } else {
        if (isDifficulty) {
            RatingGreen
        } else {
            RatingRed
        }
    }

    Canvas(modifier = Modifier.size(16.dp)) {
        drawCircle(
            color = color, // Circle color
            radius = size.minDimension / 2 // Radius of the circle
        )
    }
}

@Composable
fun PercentageCircle(value: Double) {
    val color: Color = if (value > 80.0) {
        RatingGreen
    } else if (value > 60.0) {
        RatingYellow
    } else {
        RatingRed
    }

    Canvas(modifier = Modifier.size(16.dp)) {
        drawCircle(
            color = color,
            radius = size.minDimension / 2
        )
    }
}
