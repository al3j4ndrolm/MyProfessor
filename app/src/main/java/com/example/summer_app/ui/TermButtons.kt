package com.example.summer_app.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.summer_app.TermData

@Composable
fun TermButtons(termDataList: List<TermData>, termCodeUpdaters: List<()->Unit>, selectedIndex: Int) {
    var selectedTermIndex by remember { mutableIntStateOf(selectedIndex) }

    Row {
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
        colors = ButtonDefaults.buttonColors(buttonColor))
    {
        Text(text = termText)
    }
}

private val BUTTON_SELECTED_COLOR = Color.DarkGray
private val BUTTON_UNSELECTED_COLOR = Color.LightGray