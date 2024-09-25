package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bizarrdev.MyProfessor.ui.theme.DashboardHeaderBackground


@Composable
fun HeaderBackground(height:Dp, width: Dp, content : @Composable ()->Unit){
    // Box that has gray background
    Box(
        Modifier
            .shadow(
                elevation = 8.dp, // Shadow elevation for natural look
                shape = RoundedCornerShape(16.dp), // Rounded corners like a button
                clip = false // Make sure the shadow is outside the shape
            )
            .width(width)
            .height(height)
            .background(color = DashboardHeaderBackground,
                shape = RoundedCornerShape( bottomStart = 14.dp, bottomEnd = 14.dp)
            ),
        contentAlignment = Alignment.BottomCenter
    ){
        // Box to contain text and logo
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            content()
        }
    }
}