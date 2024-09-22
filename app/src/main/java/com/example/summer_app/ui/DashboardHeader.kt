package com.example.summer_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.summer_app.R

@Composable
fun DashboardHeader() {

    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(20.dp))
            Box(Modifier
                .shadow(
                    elevation = 8.dp, // Shadow elevation for natural look
                    shape = RoundedCornerShape(16.dp), // Rounded corners like a button
                    clip = false // Make sure the shadow is outside the shape
                )
                .width(159.dp)
                .height(123.dp)
                .background(color = Color(0xFFE8E8E8),
                    shape = RoundedCornerShape( bottomStart = 14.dp, bottomEnd = 14.dp)
                ),
                contentAlignment = Alignment.BottomCenter
            ){
                Box(){
                    Row(modifier = Modifier
                        .padding(start = 58.dp,bottom = 33.dp)) {
                        Box(modifier = Modifier
                            .padding(3.dp)
                            .width(16.dp)
                            .height(16.dp)
                            .background(color = Color(0xFFC1FF72), shape = RoundedCornerShape(10.dp))
                        )
                        Box(modifier = Modifier
                            .padding(3.dp)
                            .width(16.dp)
                            .height(16.dp)
                            .background(color = Color(0xFFFFBD59), shape = RoundedCornerShape(10.dp))
                        )
                        Box(modifier = Modifier
                            .padding(3.dp)
                            .width(16.dp)
                            .height(16.dp)
                            .background(color = Color(0xFFD86161), shape = RoundedCornerShape(10.dp))
                        )
                    }
                }
                Text(
                    text = "My\nProfessor",
                    style = TextStyle(
                        fontSize = 32.sp,
                        lineHeight = 30.sp,
                        fontFamily = FontFamily(Font(R.font.lato)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                    ),
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                )
            }
            Spacer(Modifier.width(28.dp))
            Image(
                painter = painterResource(id = R.drawable.dac_logo_black), contentDescription = "Logo",
                modifier = Modifier
                    .padding(top = 47.dp)
                    .width(150.dp)
            )
        }
    }
}