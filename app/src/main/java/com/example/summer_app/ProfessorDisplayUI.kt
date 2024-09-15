package com.example.summer_app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    private val blue = Color(0xFF83a2f1)


    @Composable
    fun searchResultHeader(classAndCode: String, currentTerm: String){
        Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)) {
                        append(classAndCode + "\n")
                    }
                    withStyle(style = SpanStyle(color = Color.Gray, fontSize = 20.sp, fontWeight = FontWeight.Light)) {
                        append(currentTerm)
                    }
                },
                modifier = Modifier.padding(start = 10.dp)
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
                    .padding(end = 10.dp
                    )
            )
        }
    }

    @Composable
    fun professorInformationDisplay(professor: Professor) {
        Box(modifier = Modifier
            .padding(start = 10.dp, end = 8.dp, top = 5.dp, bottom = 8.dp)
            .background(color = lightGray, shape = RoundedCornerShape(16.dp))
        ) {
            Box(){
                Column(Modifier.padding(start = 6.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(6.dp)) {

                        Column(Modifier) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)) {
                                        append(professor.name)
                                    }
                                    append(" ")
                                    withStyle(style = SpanStyle(color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)) {
                                        append("${professor.num_ratings} reviews")
                                    }
                                },
                                textAlign = TextAlign.Start
                            )

                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .width(220.dp)
                                    .padding(start = 3.dp, top = 4.dp, bottom = 8.dp),
                                color = Color.Black
                            )
                            for (schedule in professor.schedule){
                                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                                    Text(
                                        text = schedule.substringBefore("/"),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(end = 5.dp)
                                    )
                                    displayClassroom(schedule)
                                }

                            }
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .width(220.dp)
                                    .padding(start = 3.dp, top = 8.dp, bottom = 8.dp),
                                color = Color.Black
                            )

                            Column {
                                ratingDisplay("Difficulty", professor.difficulty, false, true)
                                ratingDisplay("Would take again", professor.would_take_again, true, false)
                                ratingDisplay("Overall rating", professor.overall_rating, false, false)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun displayClassroom(schedule: String){
        val classRoom = schedule.substringAfter("/")

        Box(
            modifier = Modifier
            .background(color = blue, shape = RoundedCornerShape(10.dp))
            .padding(start = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(classRoom, fontSize = 12.sp,
                modifier = Modifier
                .padding(top = 2.dp, bottom = 2.dp, start = 8.dp, end = 8.dp),
                color = Color.White, fontWeight = FontWeight.Bold)
        }
        
    }

    @Composable
    fun ratingDisplay(text: String, value: Double, isPercentage: Boolean, isDifficulty: Boolean){

        if (isPercentage) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
                percentageCircle(value)
                Text(
                    "${text}: ${value.toInt()} %",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
        }
        else{
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
            valueCircle(value, isDifficulty)
                Text("${text}: ${value} / 5",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
        }
    }

    @Composable
    fun valueCircle(value: Double , isDifficulty: Boolean){

        var color: Color

        if (value > 4.0){
            if (isDifficulty){
                color = red
            } else {
                color = green
            }
        }

        else if (value > 3.0){
            color = orange
        }

        else {
            if (isDifficulty){
                color = green
            } else {
                color = red
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
    fun percentageCircle(value: Double){
        var color: Color

        if (value > 80.0){
            color = green
        }

        else if (value > 60.0){
            color = orange
        }

        else {
            color = red
        }

        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(
                color = color,
                radius = size.minDimension / 2
            )
        }
    }
}