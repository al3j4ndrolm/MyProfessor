package com.example.summer_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.summer_app.data.TermData

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

    Button(
        onClick = { onTerm() },
        colors = ButtonDefaults.buttonColors(buttonColor),
        shape = RoundedCornerShape(size = 14.dp), // Set shape directly in Button
        modifier = Modifier
            .padding(8.dp) // Padding around the button
            .width(152.dp)
            .height(41.dp) // Size of the button itself
    )
    {
        Text(text = termText)
    }
}

private val BUTTON_SELECTED_COLOR = Color.DarkGray
private val BUTTON_UNSELECTED_COLOR = Color.LightGray