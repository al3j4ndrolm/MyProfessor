package com.example.summer_app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.summer_app.screens.MainScreen.Companion.APP_DEFAULT_FONT

@Composable
fun SearchBoxGuideText() {
    Text(
        "Look for professor...",
        fontFamily = APP_DEFAULT_FONT,
        modifier = Modifier
            .padding(start = 18.dp, top = 50.dp, bottom = 8.dp),
        fontSize = 24.sp
    )
}
