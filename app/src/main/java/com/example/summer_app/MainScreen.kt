package com.example.summer_app

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.summer_app.ui.DashboardHeader
import com.example.summer_app.ui.SearchButton
import com.example.summer_app.ui.TermButtons
import java.util.stream.Collectors.toList

class MainScreen {
    private val defaultFont = FontFamily(Font(R.font.futura))
    private val professorDisplayUI = ProfessorDisplayUI()
    private var chosenTerm : TermData? = null
    private var isTermsSelected : Boolean = false

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun Launch(context: Context) {
        SearchScreen(context)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun SearchScreen(context: Context) {

        var searchStartTime by remember { mutableLongStateOf(0L) }
        var className by remember { mutableStateOf("") }
        var showSearchResult by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                DashboardHeader()
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
                                if (isTermsSelected) {
                                    showSearchResult = true
                                }
                            },
                            enabled = isTermsSelected
                        )
                    }

                    TermOptionsRow(
                        context = context,
                        updateChosenTerm = {
                            chosenTerm = it
                        },
                    )
                }
            }

            if (showSearchResult) {
                val (department, code) = parseCourseInfo(className.trimEnd())
                val professors = searchProfessors(
                    department.uppercase(), code.uppercase(),
                    chosenTerm!!.termCode, context
                )

                Box(
                    Modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                        .background(Color.White)
                        .padding(top = 60.dp),
                ) {
                    Column {
                        professorDisplayUI.SearchResultHeader(
                            "${department.uppercase()} ${code.uppercase()}",
                            chosenTerm!!.termText,
                            onClickBackButton = {
                                showSearchResult = false
                                className = ""
                            }
                        )
                        if (professors.isEmpty()) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        modifier = Modifier
                                            .size(100.dp),
                                        colorFilter = ColorFilter.tint(Color.LightGray),
                                        painter = painterResource(R.drawable.person_search_24px),
                                        contentDescription = "Search professor"
                                    )
                                    Text(
                                        "Searching for professors...",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = String.format(
                                    "latency: %s seconds",
                                    (System.currentTimeMillis() - searchStartTime).toDouble() / 1000.0
                                ), fontSize = 10.sp
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                for (professor in professors) {
                                    professorDisplayUI.ProfessorInformationDisplay(professor)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TermOptionsRow(context: Context, updateChosenTerm: (TermData) -> Unit) {
        val termDataList = remember { mutableStateListOf<TermData>() }

        LaunchedEffect(Unit) {
            fetchAvailableTerms(
                context = context,
                onResultReceived = {
                    termDataList.addAll(it)
                    if (termDataList.isNotEmpty()) {
                        chosenTerm = termDataList[0]
                    }
                    isTermsSelected = true
                }
            )
        }

        Row {
            if (termDataList.isNotEmpty()) {
                TermButtons(
                    termDataList = termDataList,
                    termCodeUpdaters = termDataList.stream().map { { updateChosenTerm(it) } }
                        .collect(toList()),
                )
            } else {
                Text(text = "Fetching terms...")
            }
        }
    }

    private fun parseCourseInfo(course: String): Pair<String, String> {
        val parts = course.split(" ", limit = 2)
        val department = parts.getOrElse(0) { "" }
        val code = parts.getOrElse(1) { "" }
        return Pair(department, code)
    }
}