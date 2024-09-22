package com.example.summer_app.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.summer_app.ui.TermButtons
import com.example.summer_app.usecase.DataManager
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
                        val (searchInput1, searchInput2) = parseCourseInfo(searchInput)
                        searchInfo.department = searchInput1
                        searchInfo.courseCode = searchInput2
                        onEnterLoadingScreen()
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

//            RecentSearchRow()

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
                        .padding(16.dp),
                ) {
                    items(professors) { item ->
                        ProfessorInformationDisplay(
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
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BackToSearchScreenButton(onClick = {
                onClickBackButton()
            })

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)) {
                        append("${searchInfo.department} ${searchInfo.courseCode}\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                    ) {
                        append(searchInfo.term!!.termText)
                    }
                },
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )
            androidx.compose.material3.Divider(
                modifier = Modifier
                    .height(64.dp) // Height of the divider
                    .width(2.dp), // Width (thickness) of the divider
                color = Color.Black // Customize the color as needed
            )
            DeAnzaCollegeLogo()
        }
    }

    private fun parseCourseInfo(searchInput: String): Pair<String, String> {
        val input = searchInput.trimEnd()
        val partsBySpace = input.split(" ", limit = 2)
        val parts = if (partsBySpace.size == 2) {
            partsBySpace
        } else {
            val index = input.indexOfFirst { it.isDigit() }
            listOf(input.substring(0, index - 1), input.substring(index))
        }
        val department = parts.getOrElse(0) { "" }.uppercase()
        val code = parts.getOrElse(1) { "" }.uppercase()
        return Pair(department, code)
    }

    companion object {
        val APP_DEFAULT_FONT = FontFamily(Font(R.font.lato))
    }
}