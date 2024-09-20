package com.example.summer_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ProfessorDisplayUI {

    private val lightGray = Color(0xFFedecec)
    private val green = Color(0xFFc2ff72)
    private val red = Color(0xFFd86161)
    private val orange = Color(0xFFf6be6b)
    private val codeOrange = Color(0xFFffbd59)
    private val blue = Color(0xFF83a2f1)
    private val scheduleGray = Color(0xFFd1cece)


    @Composable
    fun SearchResultHeader(classAndCode: String, currentTerm: String, onClickBackButton: () -> Unit = {}) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            BackSearchResultButton(onClick = {
                onClickBackButton()
            })

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)) {
                        append(classAndCode + "\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                    ) {
                        append(currentTerm)
                    }
                },
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )
            Divider(
                modifier = Modifier
                    .height(64.dp) // Height of the divider
                    .width(2.dp), // Width (thickness) of the divider
                color = Color.Black // Customize the color as needed
            )
            Image(
                painter = painterResource(R.drawable.dac_logo_black),
                "De Anza College Logo",
                modifier = Modifier
                    .size(height = 70.dp, width = 180.dp)
                    .padding(
                        end = 10.dp
                    )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun ProfessorInformationDisplay(professor: Professor) {
        var ratingData : ProfessorRatingData? by remember { mutableStateOf(null) }

        ratingData = getProfessorRatings(professor.name, LocalContext.current)
        professor.ratingData = ratingData

        var showSchedule by remember { mutableStateOf(false) }

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
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(professor.name)
                                    }
//                                    append(" ")
//                                    withStyle(
//                                        style = SpanStyle(
//                                            color = Color.Gray,
//                                            fontSize = 10.sp,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    ) {
//                                        append("${professor.ratingData.review_num} reviews")
//                                    }
                                },
                                textAlign = TextAlign.Start
                            )

                            Divider(Color.Black, startText = "Schedules", isClickable = true, onClick = {showSchedule = !showSchedule })


                            if (showSchedule) {
                                for ((code, schedules) in professor.all_schedules) {

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
                                                        DisplayTag(schedule, isClassRoom = true, color = blue, textColor = Color.White)
                                                    }
                                                }
                                            }
                                        }
                                        DisplayTag(code, color = codeOrange, textColor = Color.Black)
                                    }
                                }
                            }

                            Divider(Color.Black, startText = "Ratings")
                            Column {
                                if (professor.ratingData != null) {
                                    if (professor.ratingData!!.review_num == 0) {
                                        Text("No ratings yet")
                                    } else {
                                        RatingDisplay("Difficulty", professor.ratingData!!.difficulty,
                                            isPercentage = false,
                                            isDifficulty = true
                                        )
                                        RatingDisplay(
                                            text = "Would take again",
                                            professor.ratingData!!.would_take_again,
                                            isPercentage = true,
                                            isDifficulty = false
                                        )
                                        RatingDisplay(
                                            text = "Overall rating",
                                            professor.ratingData!!.overall_rating,
                                            isPercentage = false,
                                            isDifficulty = false
                                        )
                                    }
                                } else {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.width(300.dp)) {
                                        Text(
                                            text ="Fetching professor ratings...",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BackSearchResultButton(onClick: () -> Unit) {
        Image(
            painter = painterResource(R.drawable.arrow_back_ios_new_24px),
            contentDescription = "Back",
            colorFilter = ColorFilter.tint(scheduleGray),
            modifier = Modifier
                .size(30.dp)
                .padding(start = 10.dp)
                .clickable { onClick() }
        )
    }

    @Composable
    fun Divider(color: Color, startText: String = "", isClickable: Boolean = false, onClick: () -> Unit = {}){

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
            if (isClickable) {
                Text(startText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 0.dp)
                        .clickable { onClick() }
                )
            } else {
                Text(startText,
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
                    "${text}: $value / 5",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
        }
    }

    @Composable
    fun ValueCircle(value: Double, isDifficulty: Boolean) {
        val color: Color = if (value > 4.0) {
            if (isDifficulty) {
                red
            } else {
                green
            }
        } else if (value > 3.0) {
            orange
        } else {
            if (isDifficulty) {
                green
            } else {
                red
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
            green
        } else if (value > 60.0) {
            orange
        } else {
            red
        }

        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(
                color = color,
                radius = size.minDimension / 2
            )
        }
    }
}