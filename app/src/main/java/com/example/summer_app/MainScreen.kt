package com.example.summer_app

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainScreen {

    private val defaultFont = FontFamily(Font(R.font.futura))
    private val logoMyProfessor = R.drawable.myprofessor_logo
    private val professorDisplayUI = ProfessorDisplayUI()

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun Run(context: Context) {
        SearchScreen(context)
    }

    @Composable
    fun TopScreenGraphic() {
        Spacer(Modifier.height(16.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(logoMyProfessor),
                    contentDescription = "My professor logo",
                    Modifier.size(160.dp)
                )
                Divider(
                    modifier = Modifier
                        .height(64.dp) // Height of the divider
                        .width(2.dp), // Width (thickness) of the divider
                    color = Color.Black // Customize the color as needed
                )
                Image(
                    painterResource(R.drawable.dac_logo_black),
                    contentDescription = "school logo",
                    Modifier.size(160.dp)
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun SearchScreen(context: Context) {
        var startTime by remember { mutableLongStateOf(0L) }
        var className by remember { mutableStateOf("") }
        var showSearchResult by remember { mutableStateOf(false) }
        var choosenTerm by remember { mutableStateOf("unknown") }
        var terms = getTerms(context)
        var choosenTermCode by remember { mutableStateOf("") }
        var isFirstTermSelected by remember { mutableStateOf(false) }
        var isSecondTermSelected by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                TopScreenGraphic()
                Text(
                    "Look for professor...",
                    fontFamily = defaultFont,
                    modifier = Modifier
                        .padding(start = 18.dp, top = 50.dp, bottom = 8.dp),
                    fontSize = 24.sp
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        val focusManager = LocalFocusManager.current

                        TextField(
                            value = className,
                            onValueChange = { newText ->
                                className = newText
                            },
                            label = {
                                Text(
                                    "Enter CLASS and CODE",
                                    fontFamily = defaultFont
                                )
                            },
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 16.dp
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                containerColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Gray
                            ),
                            modifier = Modifier
                                .height(56.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)

                        )
                        SearchButton(onClick = {
                            startTime = System.currentTimeMillis()
                            focusManager.clearFocus()
                            if (isFirstTermSelected || isSecondTermSelected){
                                showSearchResult = true
                            }
                             },
                            isPressed = isFirstTermSelected || isSecondTermSelected
                        )
                    }

                    Row {
                        if (terms.first.isNotEmpty()){
                            termButtons(onTerm = {
                                choosenTerm = terms.first[0]
                                choosenTermCode = terms.second[0]
                                isFirstTermSelected = true
                                isSecondTermSelected = false
                            }, terms.first[0],
                                status = isFirstTermSelected
                                )
                            termButtons(onTerm = {
                                choosenTerm = terms.first[1]
                                choosenTermCode = terms.second[1]
                                isSecondTermSelected = true
                                isFirstTermSelected = false
                            }, terms.first[1]
                                , status = isSecondTermSelected)
                        } else {
                            Text(text = "Fetching terms...")
                        }
                    }
                }
            }

            if (showSearchResult) {
                val (department, code) = parseCourseInfo(className.trimEnd())
                val professors = searchProfessors(department.uppercase(), code.uppercase(), choosenTermCode.toString(), context)

                if (professors.isEmpty()) {
                    Column {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .fillMaxHeight()
                                .background(Color.White)
                                .padding(top = 60.dp),
                        ) {
                            Column {
                                professorDisplayUI.searchResultHeader("${department.uppercase()} ${code.uppercase()}", "$choosenTerm")
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Image(
                                            modifier = Modifier
                                                .size(100.dp),
                                            colorFilter = ColorFilter.tint(Color.LightGray),
                                            painter = painterResource(R.drawable.person_search_24px),
                                            contentDescription = "Search professor")
                                        Text("Searching for professors...", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.LightGray)
                                    }
                                }
                            }
                        }
                    }

                } else {
                    val endTime = System.currentTimeMillis()

                    Box(
                        Modifier
                            .fillMaxSize()
                            .fillMaxHeight()
                            .background(Color.White)
                            .padding(top = 60.dp),
                    ) {
                        Column {
                            professorDisplayUI.searchResultHeader("${department.uppercase()} ${code.uppercase()}", "$choosenTerm", onClick = {showSearchResult = false
                                className = ""})
                            Text(text = String.format("latency: %s seconds", (endTime - startTime).toDouble()/1000.0),
                                fontSize = 10.sp)

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            ) {
                                for (professor in professors) {
                                    professorDisplayUI.professorInformationDisplay(professor)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun parseCourseInfo(course: String): Pair<String, String> {
        val parts = course.split(" ", limit = 2)
        val department = parts.getOrElse(0) { "" }
        val code = parts.getOrElse(1) { "" }
        return Pair(department, code)
    }

    @Composable
    fun SearchButton(onClick: () -> Unit, isPressed: Boolean) {
        val unPressedColor = Color(0xFF969696)
        val pressedColor = Color.DarkGray
        var statusColor = unPressedColor

        if (isPressed) {
            statusColor = pressedColor
        }

        Button(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray, // Background color
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painterResource(
                    R.drawable.search_24dp_434343_fill0_wght400_grad0_opsz24
                ),
                "search",
                modifier = Modifier.size(35.dp),
                colorFilter = ColorFilter.tint(statusColor)
            )
        }
    }



    @Composable
    fun termButtons(onTerm: () -> Unit, term: String, status: Boolean){

        var isClickedColor = Color.LightGray

        if (status){
            isClickedColor = Color.DarkGray
        }

        Button(onClick = { onTerm() }, colors = ButtonDefaults.buttonColors(isClickedColor)) {
            Text(text = term)
        }
    }
}