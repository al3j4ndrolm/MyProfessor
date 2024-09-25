package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bizarrdev.MyProfessor.R

@Composable
fun DeAnzaCollegeLogo(scale: Int = 6){
    Image(
        painter = painterResource(R.drawable.dac_logo_black),
        "De Anza College Logo",
        modifier = Modifier
            .width((scale * 20).dp)
            .height((scale * 10).dp)
    )
}