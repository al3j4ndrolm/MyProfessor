package com.example.summer_app.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.summer_app.R
import com.example.summer_app.data.Professor
import com.example.summer_app.data.ScreenSection
import com.example.summer_app.data.ScreenSection.SEARCH_INPUT
import com.example.summer_app.data.ScreenSection.SEARCH_LOADING
import com.example.summer_app.data.ScreenSection.SEARCH_RESULT
import com.example.summer_app.data.SearchInfo
import com.example.summer_app.data.TermData
import com.example.summer_app.ui.DashboardHeader
import com.example.summer_app.ui.DeAnzaCollegeLogo
import com.example.summer_app.ui.SearchBoxGuideText
import com.example.summer_app.ui.SearchButton
import com.example.summer_app.ui.SearchInputTextField
import com.example.summer_app.ui.SearchLoadingScene
import com.example.summer_app.ui.Snackbar
import com.example.summer_app.ui.TermButtons
import com.example.summer_app.ui.theme.selectObjectGray
import com.example.summer_app.usecase.DataManager
import com.example.summer_app.usecase.parseInputString
import java.util.stream.Collectors.toList

class MainScreen {
    private val dataManager: DataManager = DataManager()
    private var professors: List<Professor> = listOf()
    private var searchInput: String = ""
    private var searchInfo: SearchInfo = SearchInfo()

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun Launch(context: Context) {
        var screenSection: ScreenSection by remember { mutableStateOf(SEARCH_INPUT) }

        when (screenSection) {
            SEARCH_INPUT -> SearchInputScreen(
                context = context,
                onEnterLoadingScreen = {
                    screenSection = SEARCH_LOADING
                }
            )

            SEARCH_LOADING -> SearchLoadingScreen(
                context = context,
                onEnterResultScreen = {
                    screenSection = SEARCH_RESULT
                },
                onBackToSearchScreen = {
                    screenSection = SEARCH_INPUT
                }
            )

            SEARCH_RESULT -> ResultScreen(
                onBackToSearchScreen = {
                    screenSection = SEARCH_INPUT
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun SearchInputScreen(context: Context, onEnterLoadingScreen: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var isReadyToSearch by remember { mutableStateOf(searchInfo.isReady()) }

            DashboardHeader()
            SearchBoxGuideText()

            Row(verticalAlignment = Alignment.CenterVertically) {
                SearchInputTextField {
                    searchInput = it
                    isReadyToSearch = searchInfo.term != null && searchInput.isNotBlank()
                }

                Spacer(modifier = Modifier.width(20.dp))

                SearchButton(
                    onClick = {
                        val parsedInput = parseInputString(searchInput)
                        println(String.format("parsed input: %s", parsedInput))
                        if (parsedInput != null) {
                            val (searchInput1, searchInput2) = parsedInput
                            searchInfo.department = searchInput1
                            searchInfo.courseCode = searchInput2
                            onEnterLoadingScreen()
                        } else {
                            // Error handle
                        }
                    },
                    enabled = isReadyToSearch
                )
            }

            Box(Modifier
                .fillMaxWidth(), contentAlignment = Alignment.Center) {
                TermOptionsRow(
                    context = context,
                    updateChosenTerm = {
                        searchInfo.term = it
                        isReadyToSearch = searchInput.isNotBlank()
                    },
                )
            }
        }
    }

    @Composable
    fun SearchLoadingScreen(
        context: Context,
        onEnterResultScreen: () -> Unit,
        onBackToSearchScreen: () -> Unit
    ) {
        LaunchedEffect(Unit) {
            dataManager.startSearchingProfessors(
                context = context,
                department = searchInfo.department,
                courseCode = searchInfo.courseCode,
                term = searchInfo.term!!.termCode,
                onResultReceived = {
                    professors = it
                    onEnterResultScreen()
                }
            )
        }
        SearchResultHeader(
            onClickBackButton = {
                dataManager.stopSearchingProfessors()
                onBackToSearchScreen()
            }
        )
        SearchLoadingScene()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun ResultScreen(onBackToSearchScreen: () -> Unit) {
        Column {
            SearchResultHeader(
                onClickBackButton = onBackToSearchScreen
            )

            if (professors.isEmpty()) {
                Text("NADA is found", textAlign = TextAlign.Center, fontSize = 40.sp)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                ) {
                    items(professors) { item ->
                        ProfessorCard(
                            dataManager = dataManager,
                            professor = item,
                            department = searchInfo.department
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TermOptionsRow(context: Context, updateChosenTerm: (TermData) -> Unit) {
        if (dataManager.availableTerms.isNotEmpty()) {
            val availableTerms = dataManager.availableTerms
            TermButtons(
                termDataList = availableTerms,
                termCodeUpdaters = availableTerms.stream().map { { updateChosenTerm(it) } }
                    .collect(toList()),
                selectedIndex = availableTerms.indexOf(searchInfo.term)
            )
            return
        }

        var hasTerms by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            dataManager.fetchAvailableTerms(
                context = context,
                onResultReceived = {
                    hasTerms = true
                    if (it.isNotEmpty()) {
                        searchInfo.term = it[0]
                    }
                }
            )
        }

        if (hasTerms) {
            val availableTerms = dataManager.availableTerms
            TermButtons(
                termDataList = availableTerms,
                termCodeUpdaters = availableTerms.stream().map { { updateChosenTerm(it) } }
                    .collect(toList()),
                selectedIndex = 0,
            )
        } else {
            Text(text = "Fetching terms...")
        }
    }

    @Composable
    fun SearchResultHeader(
        onClickBackButton: () -> Unit = {}
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(Modifier
                .shadow(
                    elevation = 6.dp, // Softer elevation for more natural shadow
                    shape = RoundedCornerShape(14.dp), // Match the shape of the TextField
                    clip = false // Allow the shadow to be drawn outside the shape
                )
                .width(340.dp)
                .height(153.dp)
                .background(color = Color(0xFFE8E8E8), shape = RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp))){

                Column {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                        Row(Modifier.padding(start = 5.dp,top = 40.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "",
                                modifier = Modifier.clickable { onClickBackButton() },
                                tint = selectObjectGray
                            )
                            ballsRow()
                        }
                    }
                    Box(
                        Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight(700),
                                        color = Color(0xFF000000)
                                    )
                                ) {
                                    append("${searchInfo.department} ${searchInfo.courseCode}\n")
//                                    append("TEST MODE\n")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 20.sp, // Smaller font size for "Fall 2024"
                                        fontWeight = FontWeight(400), // Lighter weight for "Fall 2024"
                                        color = Color(0xFF888888) // Lighter color
                                    )
                                ) {
                                    append(searchInfo.term!!.termText) // Smaller and lighter "Fall 2024"
//                                    append("Testing") // Smaller and lighter "Fall 2024"

                                }
                            },
                            lineHeight = 30.sp,
                            fontFamily = APP_DEFAULT_FONT,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(R.drawable.dac_logo_black),
                        "De Anza College Logo",
                        modifier = Modifier
                            .width(124.dp)
                            .height(63.dp)
                            .padding(
                                end = 20.dp
                            )
                    )
                }
            }
        }
    }

    companion object {
        val APP_DEFAULT_FONT = FontFamily(Font(R.font.lato))
    }

    @Composable
    fun ballsRow() {
        Row(modifier = Modifier
            .padding(start = 5.dp)) {
            Box(modifier = Modifier
                .padding(3.dp)
                .width(16.dp)
                .height(16.dp)
                .background(color = Color(0xFFC1FF72), shape = RoundedCornerShape(10.dp))
            )
            Box(modifier = Modifier
                .padding(3.dp)
                .width(16.dp)
                .height(16.dp)
                .background(color = Color(0xFFFFBD59), shape = RoundedCornerShape(10.dp))
            )
            Box(modifier = Modifier
                .padding(3.dp)
                .width(16.dp)
                .height(16.dp)
                .background(color = Color(0xFFD86161), shape = RoundedCornerShape(10.dp))
            )
        }
    }
}