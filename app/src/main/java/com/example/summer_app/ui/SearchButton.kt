package com.example.summer_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.summer_app.R

@Composable
fun SearchButton(onClick: () -> Unit, enabled: Boolean) {
    val statusColor = if (enabled) ENABLED_COLOR else DISABLED_COLOR

    Button(
        onClick = onClick,
        Modifier
            .width(80.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray, // Background color
        ),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled,
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "" // Add a valid content description
        )
    }
}

private val DISABLED_COLOR = Color(0xFF969696)
private val ENABLED_COLOR = Color.DarkGray