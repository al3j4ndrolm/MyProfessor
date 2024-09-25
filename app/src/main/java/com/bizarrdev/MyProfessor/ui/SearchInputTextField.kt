package com.bizarrdev.MyProfessor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.R

@Composable
fun SearchInputTextField(onUpdateInputText: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    var inputText by remember { mutableStateOf("") }

    TextField(
        value = inputText,
        singleLine = true,
        onValueChange = { newText ->
            if (newText.length <= 10){
                inputText = newText
                onUpdateInputText(newText)
            }
        },
        placeholder = {
            Text(
                text = "ex. MATH 1A",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 30.sp,
                    fontFamily = FontFamily(Font(R.font.lato)),
                    fontWeight = FontWeight(400),
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFA4A4A4),
                )
            )
        },
        shape = RoundedCornerShape(32.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            focusedContainerColor = Color(0xFFE8E8E8),
            unfocusedContainerColor = Color(0xFFE8E8E8),
            disabledContainerColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        modifier = Modifier
            .shadow(
                elevation = 6.dp, // Softer elevation for more natural shadow
                shape = RoundedCornerShape(14.dp), // Match the shape of the TextField
                clip = false // Allow the shadow to be drawn outside the shape
            )
            .width(236.dp)
            .wrapContentHeight() // Adjust height according to the content
            .background(color = Color(0xFFE8E8E8), shape = RoundedCornerShape(size = 14.dp))
            ,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()  // Hide keyboard when "Done" is pressed
            }
        )
    )

}