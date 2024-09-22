package com.example.summer_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.summer_app.data.TermData
import com.example.summer_app.screens.MainScreen.Companion.APP_DEFAULT_FONT

@Composable
fun TermButtons(termDataList: List<TermData>, termCodeUpdaters: List<()->Unit>, selectedIndex: Int) {
    var selectedTermIndex by remember { mutableIntStateOf(selectedIndex) }

    Row(modifier = Modifier.padding(top = 21.dp).fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center) {
        for (i in termDataList.indices){
            TermButton(
                onTerm = {
                    termCodeUpdaters[i]()
                    selectedTermIndex = i
                },
                termText = termDataList[i].termText,
                isSelected = selectedTermIndex == i
            )

        }
    }
}

@Composable
private fun TermButton(onTerm: () -> Unit, termText: String, isSelected: Boolean){
    val buttonColor = if (isSelected) BUTTON_SELECTED_COLOR else BUTTON_UNSELECTED_COLOR

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .shadow(
                elevation = 4.dp, // Shadow elevation for a natural look
                shape = RoundedCornerShape(14.dp), // Same rounded corners as the button
                clip = false // Ensure shadow is outside the shape
            )
            .background(
                color = buttonColor, // Button's background color
                shape = RoundedCornerShape(14.dp) // Match the shape of the Box to the button
            )
            .width(152.dp)
            .height(41.dp)

            .clickable(onClick = { onTerm() }), // Make the box behave like a button (clickable),
        contentAlignment = Alignment.Center // Center the text inside the box
    ) {
        Text(
            text = termText,
            fontFamily = APP_DEFAULT_FONT,
            color = Color.White // Customize text color if needed
        )
    }

}

private val BUTTON_SELECTED_COLOR = Color.DarkGray
private val BUTTON_UNSELECTED_COLOR = Color.LightGray