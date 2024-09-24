package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bizarrdev.MyProfessor.R
import com.bizarrdev.MyProfessor.ui.theme.BackToSearchButtonColor

@Composable
fun BackToSearchScreenButton(onClick: () -> Unit) {
    Icon(
        Icons.Filled.ArrowBack,
        contentDescription = "",
        modifier = Modifier.clickable { onClick() },
        tint = BackToSearchButtonColor
    )
}