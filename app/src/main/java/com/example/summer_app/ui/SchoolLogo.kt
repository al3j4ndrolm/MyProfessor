package com.example.summer_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.summer_app.R

@Composable
fun DeAnzaCollegeLogo(){
    Image(
        painter = painterResource(R.drawable.dac_logo_black),
        "De Anza College Logo",
        modifier = Modifier
            .size(height = 70.dp, width = 180.dp)
            .padding(
                end = 10.dp
            )
    )
}