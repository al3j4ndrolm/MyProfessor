package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.R
import com.bizarrdev.MyProfessor.ui.theme.APP_DEFAULT_FONT
import com.bizarrdev.MyProfessor.ui.theme.SearchGuideText

@Composable
fun SearchBoxGuideText() {

    Spacer(modifier = Modifier.height(56.dp))
    Box(Modifier.fillMaxWidth()) {
        Text(
            text = "Search professors by class",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 30.sp,
                fontFamily = APP_DEFAULT_FONT,
                fontWeight = FontWeight(700),
                color = SearchGuideText
            ),
            modifier = Modifier
                .padding(start = 32.dp, bottom = 5.dp)
        )
    }
}
