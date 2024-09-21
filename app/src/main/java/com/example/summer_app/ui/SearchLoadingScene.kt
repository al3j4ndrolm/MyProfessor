package com.example.summer_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.summer_app.R

@Composable
fun SearchLoadingScene() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier
                    .size(100.dp),
                colorFilter = ColorFilter.tint(Color.LightGray),
                painter = painterResource(R.drawable.person_search_24px),
                contentDescription = "Search professor"
            )
            Text(
                "Searching for professors...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
        }
    }
}