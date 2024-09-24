package com.example.MyProfessor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MyProfessor.screens.MainScreen.Companion.APP_DEFAULT_FONT
import com.example.MyProfessor.ui.theme.SearchLoadingTextColor

@Composable
fun SearchLoadingScene() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Searching professors...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = APP_DEFAULT_FONT,
                color = SearchLoadingTextColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}