package com.example.summer_app.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.summer_app.MainScreen.Companion.APP_DEFAULT_FONT

@Composable
fun SearchInputTextField(onUpdateInputText : (String)->Unit){
    val focusManager = LocalFocusManager.current
    var inputText by remember { mutableStateOf("") }

    TextField(
        value = inputText,
        onValueChange = { newText ->
            inputText = newText
            onUpdateInputText(newText)
        },
        label = {
            Text(
                "Enter CLASS and CODE",
                fontFamily = APP_DEFAULT_FONT
            )
        },
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp,
            bottomStart = 16.dp
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray,
            disabledContainerColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        modifier = Modifier
            .height(56.dp).onFocusChanged {
                // Clear focus when focus is lost or on some condition
                if (!it.isFocused) {
                    println("Not focus anymore!!")
                    focusManager.clearFocus()
                }
            },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )
}