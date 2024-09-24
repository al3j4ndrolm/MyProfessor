package com.bizarrdev.MyProfessor.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.data.Professor
import com.bizarrdev.MyProfessor.data.ProfessorRatingData
import com.bizarrdev.MyProfessor.ui.RatingsSection
import com.bizarrdev.MyProfessor.ui.ScheduleSection
import com.bizarrdev.MyProfessor.ui.theme.ProfessorReviewCount
import com.bizarrdev.MyProfessor.usecase.DataManager

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
            }
        )
    }

    Box(
        modifier = Modifier
            .padding(end = 8.dp, top = 8.dp)
            .wrapContentHeight()
            .animateContentSize()
    ) {
        Box {
            Column(Modifier.padding()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding()
                ) {
                    Column(Modifier) {
                        ProfessorNameAndReviewCount(
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
private fun ProfessorNameAndReviewCount(professorName: String, ratingData: ProfessorRatingData?) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(professorName)
            }
            if (ratingData != null) {
                append(" ")
                withStyle(
                    style = SpanStyle(
                        color = ProfessorReviewCount,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append("${ratingData.review_num} reviews")
                }
            }
        },
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(start = 15.dp)
    )
}



