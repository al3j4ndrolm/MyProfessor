package com.bizarrdev.MyProfessor.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bizarrdev.MyProfessor.ui.theme.ProfessorCardBackground


@Composable
fun RatingsSectionRowBackground(sectionRowContent: @Composable ()->Unit){
    // Invisible box
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .wrapContentHeight()
            .animateContentSize()
    ) {
        // Row with color
        Box(
            modifier = Modifier
                .background(ProfessorCardBackground, shape = RoundedCornerShape(14.dp))
                .padding(horizontal = 4.dp, vertical = 5.dp)
                .wrapContentHeight()
                .animateContentSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            sectionRowContent()
        }
    }
}

@Composable
fun ScheduleSectionRowBackground(sectionRowContent: @Composable ()->Unit){
    // Invisible box
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .wrapContentHeight()
            .animateContentSize()
    ) {
        // Row with color
        Box(
            modifier = Modifier
                .background(
                    color = ProfessorCardBackground,
                    shape = RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
                .padding(start = 14.dp, end = 4.dp)
                .padding(vertical = 5.dp)
                .wrapContentHeight()
                .animateContentSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            sectionRowContent()
        }
    }
}