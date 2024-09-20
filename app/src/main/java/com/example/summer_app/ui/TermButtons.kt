package com.example.summer_app.ui

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
fun TermButtons(termDataList: List<TermData>, termCodeUpdaters: List<()->Unit>) {
    var selectedTermIndex by remember { mutableIntStateOf(0) }
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

@Composable
fun TermButton(onTerm: () -> Unit, termText: String, isSelected: Boolean){
    val buttonColor = if (isSelected) Color.DarkGray else Color.LightGray

    Button(
        onClick = { onTerm() },
        colors = ButtonDefaults.buttonColors(buttonColor))
    {
        Text(text = termText)
    }
}