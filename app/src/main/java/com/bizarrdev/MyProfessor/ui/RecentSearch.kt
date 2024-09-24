package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.R

@Composable
fun RecentSearchRow() {
    val recentSearches = listOf("MATH 1A", "MATH 210X", "MATH 220C")
    RecentSearchText()

    if (recentSearches.isEmpty()) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            RecentSearchText("No recent searches")
        }
    }
    Row(Modifier.padding(start = 20.dp, top = 10.dp)) {

        for (search in recentSearches) {
            RecentSearchItem(search = search)
        }
    }
}

@Composable
fun RecentSearchItem(search: String) {
    Box(modifier = Modifier
        .padding(6.dp)
        .background(color = Color(0xFFD7D7D7), shape = RoundedCornerShape(size = 18.dp)),
        contentAlignment = Alignment.Center

    ) {
        Text(text = search,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily(Font(R.font.lato)),
                fontWeight = FontWeight(800),
                color = Color(0xFFA4A4A4),
            ),
            modifier = Modifier
                .padding(top = 6.dp, start = 20.dp, end = 20.dp, bottom = 6.dp)
        )
    }
}

@Composable
fun RecentSearchText(text : String = "Recent search") {
    Box(Modifier.fillMaxWidth()){
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily(Font(R.font.lato)),
                fontWeight = FontWeight(800),
                color = Color(0xFFA4A4A4),
            ),
            modifier = Modifier
                .padding(start = 32.dp, top = 27.dp)
        )
    }
}