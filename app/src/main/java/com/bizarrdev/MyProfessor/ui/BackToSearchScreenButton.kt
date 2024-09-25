package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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