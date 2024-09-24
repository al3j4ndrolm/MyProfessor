package com.bizarrdev.MyProfessor.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.screens.MainScreen.Companion.APP_DEFAULT_FONT
import com.bizarrdev.MyProfessor.ui.theme.ClassLocationLabelBackground
import com.bizarrdev.MyProfessor.ui.theme.ClassLocationLabelText
import com.bizarrdev.MyProfessor.ui.theme.OneScheduleBlockBackground
import com.bizarrdev.MyProfessor.ui.theme.ProfessorCardBackground
import com.bizarrdev.MyProfessor.ui.theme.ScheduleButtonText
import com.bizarrdev.MyProfessor.ui.theme.ScheduleTextColor


@Composable
fun ScheduleSection(allSchedules: Map<String, List<String>>) {
    var showSchedule by remember { mutableStateOf(false) }

    ScheduleSectionRowBackground {
        if (showSchedule) {
            ScheduleCard(onClick = { showSchedule = false }, allSchedules = allSchedules)
        } else {
            Text(
                "Schedule",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = APP_DEFAULT_FONT,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .clickable { showSchedule = true },
                color = ScheduleButtonText,
            )
        }
    }
}

@Composable
fun ScheduleCard(onClick: () -> Unit, allSchedules: Map<String, List<String>>) {
    Box(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }),
        contentAlignment = Alignment.CenterStart
    ) {
        ScheduleContent(allSchedules = allSchedules)
    }
}

@Composable
fun ScheduleContent(allSchedules: Map<String, List<String>>) {
    Column {
        for ((code, schedules) in allSchedules) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .background(ProfessorCardBackground, RoundedCornerShape(14.dp))
            ) {
                SchedulesForOneClass(schedules)
            }
        }
    }
}

@Composable
fun SchedulesForOneClass(schedules: List<String>){
    Column (
        modifier = Modifier
            .padding(vertical = 3.dp)
            .background(
                OneScheduleBlockBackground,
                RoundedCornerShape(12.dp)
            )
            .wrapContentHeight()
            .animateContentSize(),
    ){
        for (schedule in schedules) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = schedule.substringBefore("/"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = APP_DEFAULT_FONT,
                    color = ScheduleTextColor,
                    modifier = Modifier.padding(5.dp)
                )
                DisplayTag(
                    schedule,
                    isClassRoom = true,
                    color = ClassLocationLabelBackground,
                    textColor = ClassLocationLabelText
                )
            }
        }
    }
}

@Composable
fun DisplayTag(value: String, isClassRoom: Boolean = false, color: Color, textColor: Color) {
    var text = value

    if (isClassRoom) {
        text = value.substringAfter("/")
    }

    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = APP_DEFAULT_FONT,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 8.dp, end = 8.dp),
        )
    }
    Spacer(modifier = Modifier.width(4.dp))
}

