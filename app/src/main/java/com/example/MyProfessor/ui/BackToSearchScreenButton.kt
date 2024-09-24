package com.example.MyProfessor.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.MyProfessor.R
import com.example.MyProfessor.ui.theme.BackToSearchButtonColor

@Composable
fun BackToSearchScreenButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(R.drawable.arrow_back_ios_new_24px),
        contentDescription = "Back",
        colorFilter = ColorFilter.tint(BackToSearchButtonColor),
        modifier = Modifier
            .size(30.dp)
            .padding(start = 10.dp)
            .clickable { onClick() }
    )
}