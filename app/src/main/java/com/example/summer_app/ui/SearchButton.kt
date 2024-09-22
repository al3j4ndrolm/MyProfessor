package com.example.summer_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray, // Background color
        ),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled,
    ) {
        Image(
            painterResource(
                R.drawable.search_24dp_434343_fill0_wght400_grad0_opsz24
            ),
            "dashboard search button",
            modifier = Modifier.size(35.dp),
            colorFilter = ColorFilter.tint(statusColor)
        )
    }
}

private val DISABLED_COLOR = Color(0xFF969696)
private val ENABLED_COLOR = Color.DarkGray