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
import androidx.compose.runtime.mutableIntStateOf
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
import java.util.stream.Collectors.toList

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
        var searchStartTime by remember { mutableLongStateOf(0L) }
        var className by remember { mutableStateOf("") }
        var showSearchResult by remember { mutableStateOf(false) }

        var chosenTerm : TermData? by remember { mutableStateOf(null) }

        val termDataList = getTerms(context)

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
                                .height(56.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)

                        )
                        SearchButton(
                            onClick = {
                                searchStartTime = System.currentTimeMillis()
                                focusManager.clearFocus()
                                if (termDataList.isNotEmpty()){
                                    showSearchResult = true
                                }
                             },
                            enabled = termDataList.isNotEmpty()
                        )
                    }


                    Row {
                        if (termDataList.isNotEmpty()){
                            chosenTerm = termDataList[0]

                            TermButtons(
                                termDataList = termDataList,
                                termCodeUpdaters = termDataList.stream().map { {chosenTerm=it} }.collect(toList()),
                            )
                        } else {
                            Text(text = "Fetching terms...")
                        }
                    }
                }
            }

            if (showSearchResult) {
                val (department, code) = parseCourseInfo(className.trimEnd())
                val professors = searchProfessors(department.uppercase(), code.uppercase(),
                    chosenTerm!!.termCode, context)

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
                                professorDisplayUI.searchResultHeader("${department.uppercase()} ${code.uppercase()}",
                                    chosenTerm!!.termText
                                )
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
                            professorDisplayUI.searchResultHeader("${department.uppercase()} ${code.uppercase()}",
                                chosenTerm!!.termText, onClick = {showSearchResult = false
                                className = ""})
                            Text(text = String.format("latency: %s seconds", (endTime - searchStartTime).toDouble()/1000.0),
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

    private fun parseCourseInfo(course: String): Pair<String, String> {
        val parts = course.split(" ", limit = 2)
        val department = parts.getOrElse(0) { "" }
        val code = parts.getOrElse(1) { "" }
        return Pair(department, code)
    }

    @Composable
    fun SearchButton(onClick: () -> Unit, enabled: Boolean) {
        val disabledColor = Color(0xFF969696)
        val enabledColor = Color.DarkGray
        val statusColor = if (enabled) enabledColor else disabledColor

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
}