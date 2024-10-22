package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.data.SearchInfo
import com.bizarrdev.MyProfessor.ui.theme.APP_DEFAULT_FONT
import com.bizarrdev.MyProfessor.ui.theme.RecentSearchLabel
import com.bizarrdev.MyProfessor.ui.theme.RecentSearchTextColor

@Composable
fun RecentSearchSection(recentSearch: List<SearchInfo>, onClick: (SearchInfo) -> Unit) {
    if (recentSearch.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 30.dp),
            horizontalAlignment = Alignment.Start) {
            RecentSearchText()

            for (searchInfo in recentSearch) {
                Spacer(modifier = Modifier.height(12.dp))
                RecentSearchItem(searchInfo = searchInfo, onClick = {onClick(searchInfo)})
            }
        }
    }
}

@Composable
fun RecentSearchItem(searchInfo: SearchInfo, onClick : (SearchInfo)->Unit) {
    val searchTag = "${searchInfo.department} ${searchInfo.courseCode}    ${searchInfo.term!!.termText}"
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = RecentSearchLabel, shape = RoundedCornerShape(size = 12.dp))
            .padding(horizontal = 10.dp).padding(vertical = 4.dp).clickable { onClick(searchInfo) },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = searchTag,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 30.sp,
                fontFamily = APP_DEFAULT_FONT,
                fontWeight = FontWeight(800),
                color = RecentSearchTextColor,
            )
        )
    }
}

@Composable
fun RecentSearchText(text: String = "Recent search") {
    Box(Modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 30.sp,
                fontFamily = APP_DEFAULT_FONT,
                fontWeight = FontWeight(800),
                color = Color(0xFFA4A4A4),
            )
        )
    }
}