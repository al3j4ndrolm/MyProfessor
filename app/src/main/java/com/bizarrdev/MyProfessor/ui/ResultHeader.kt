package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.data.SearchInfo
import com.bizarrdev.MyProfessor.ui.theme.APP_DEFAULT_FONT
import com.bizarrdev.MyProfessor.ui.theme.SearchHeaderCourseText
import com.bizarrdev.MyProfessor.ui.theme.SearchHeaderTermText


@Composable
fun ResultHeader(
    searchInfo: SearchInfo,
    onClickBackButton: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        HeaderBackground(height = 150.dp, width = 340.dp) {
            ResultHeaderContent(searchInfo, onClickBackButton)
        }
    }
}

@Composable
fun ResultHeaderContent(
    searchInfo: SearchInfo,
    onClickBackButton: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        ResultHeaderContentLeftSide(searchInfo, onClickBackButton)
        DeAnzaCollegeLogo(scale = 7)
    }
}

@Composable
fun ResultHeaderContentLeftSide(
    searchInfo: SearchInfo,
    onClickBackButton: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(start = 10.dp).padding(top = 32.dp)
    ) {
        Row(
            modifier = Modifier.width(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BackToSearchScreenButton(onClick = onClickBackButton)
            ThreeBallsLogo(size = 16.dp, paddingValues = 3.dp)
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 5.dp).padding(start = 1.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            ResultHeaderText(searchInfo)
        }
    }
}

@Composable
fun ResultHeaderText(searchInfo: SearchInfo) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight(700),
                    color = SearchHeaderCourseText
                )
            ) {
                append("${searchInfo.department} ${searchInfo.courseCode}\n")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 20.sp, // Smaller font size for "Fall 2024"
                    fontWeight = FontWeight(400), // Lighter weight for "Fall 2024"
                    color = SearchHeaderTermText // Lighter color
                )
            ) {
                append(searchInfo.term!!.termText) // Smaller and lighter "Fall 2024"
            }
        },
        lineHeight = 30.sp,
        fontFamily = APP_DEFAULT_FONT,
    )
}