package com.example.MyProfessor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MyProfessor.R
import com.example.MyProfessor.ui.theme.DashboardHeaderBackground
import com.example.MyProfessor.ui.theme.DashboardHeaderTextColor

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
                .background(color = DashboardHeaderBackground,
                    shape = RoundedCornerShape( bottomStart = 14.dp, bottomEnd = 14.dp)
                ),
                contentAlignment = Alignment.BottomCenter
            ){
                Box(modifier = Modifier.padding(start = 60.dp, bottom = 40.dp)){
                    ThreeBallsLogo()
                }
                Text(
                    text = "My\nProfessor",
                    style = TextStyle(
                        fontSize = 32.sp,
                        lineHeight = 30.sp,
                        fontFamily = FontFamily(Font(R.font.lato)),
                        fontWeight = FontWeight(600),
                        color = DashboardHeaderTextColor,
                    ),
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
            Spacer(Modifier.width(28.dp))
            Box(modifier = Modifier.padding(top = 50.dp)){
                DeAnzaCollegeLogoBig()
            }
        }
    }
}
