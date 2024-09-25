package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.R
import com.bizarrdev.MyProfessor.ui.theme.DashboardHeaderTextColor

@Composable
fun DashboardHeadedAndSchoolLogo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DashboardHeader()
        Box(modifier = Modifier.padding(top = 48.dp)) {
            DeAnzaCollegeLogo(scale = 9)
        }
    }
}

@Composable
private fun DashboardHeader() {
    HeaderBackground(height = 120.dp, width = 160.dp) {
        Text(
            text = "My\nProfessor",
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily(Font(R.font.lato)),
                fontWeight = FontWeight(600),
                color = DashboardHeaderTextColor,
            ),
            textAlign = TextAlign.Start
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp),
            contentAlignment = Alignment.CenterEnd,
        )
        {
            ThreeBallsLogo(size = 21.dp, paddingValues = 3.dp)
        }
    }
}
