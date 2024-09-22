package com.example.summer_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchButton(onClick: () -> Unit, enabled: Boolean) {
    val statusColor = if (enabled) ENABLED_COLOR else DISABLED_COLOR

    Box(
        modifier = Modifier
            .shadow(
                elevation = 8.dp, // Shadow elevation for natural look
                shape = RoundedCornerShape(16.dp), // Rounded corners like a button
                clip = false // Make sure the shadow is outside the shape
            )
            .background(
                color = Color.LightGray, // Background color similar to a button
                shape = RoundedCornerShape(16.dp) // Rounded corners for the box
            )
            .width(80.dp)
            .height(56.dp)
            .clickable(onClick = onClick), // Make the box clickable
        contentAlignment = Alignment.Center // Center the icon inside the box
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "Search Icon", // Add accessibility description
            tint = statusColor
        )
    }
}

private val DISABLED_COLOR = Color(0xFF969696)
private val ENABLED_COLOR = Color.DarkGray