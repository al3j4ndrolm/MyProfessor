package com.bizarrdev.MyProfessor.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizarrdev.MyProfessor.data.Professor
import com.bizarrdev.MyProfessor.data.ScreenSection
import com.bizarrdev.MyProfessor.data.ScreenSection.SEARCH_INPUT
import com.bizarrdev.MyProfessor.data.ScreenSection.SEARCH_LOADING
import com.bizarrdev.MyProfessor.data.ScreenSection.SEARCH_RESULT
import com.bizarrdev.MyProfessor.data.SearchInfo
import com.bizarrdev.MyProfessor.data.TermData
import com.bizarrdev.MyProfessor.ui.DashboardHeadedAndSchoolLogo
import com.bizarrdev.MyProfessor.ui.FadeoutCover
import com.bizarrdev.MyProfessor.ui.RecentSearchRow
import com.bizarrdev.MyProfessor.ui.ResultHeader
import com.bizarrdev.MyProfessor.ui.SearchBoxGuideText
import com.bizarrdev.MyProfessor.ui.SearchButton
import com.bizarrdev.MyProfessor.ui.SearchInputTextField
import com.bizarrdev.MyProfessor.ui.SearchLoadingScene
import com.bizarrdev.MyProfessor.ui.TermButtons
import com.bizarrdev.MyProfessor.ui.theme.APP_DEFAULT_FONT
import com.bizarrdev.MyProfessor.ui.theme.FetchingTermsTextColor
import com.bizarrdev.MyProfessor.ui.theme.NoCourseFoundTextColor
import com.bizarrdev.MyProfessor.usecase.DataManager
import com.bizarrdev.MyProfessor.usecase.parseInputString
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

        Box(modifier = Modifier.fillMaxSize().background(Color.White)){
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
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun SearchInputScreen(context: Context, onEnterLoadingScreen: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var isReadyToSearch by remember { mutableStateOf(searchInfo.isReady()) }

            DashboardHeadedAndSchoolLogo()
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
                            dataManager.updateMostRecentSearch(searchInfo)
                            onEnterLoadingScreen()
                        } else {
                            // Error handle
                        }
                    },
                    enabled = isReadyToSearch
                )
            }

            Box(
                Modifier
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                TermOptionsRow(
                    context = context,
                    updateChosenTerm = {
                        searchInfo.term = it
                        isReadyToSearch = searchInput.isNotBlank()
                    },
                )
            }

            RecentSearchRow(
                recentSearch = dataManager.recentSearch,
                onClick = {
                    searchInfo = it.copy()
                    dataManager.updateMostRecentSearch(searchInfo)
                    onEnterLoadingScreen()
                })
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
        ResultHeader(
            searchInfo = searchInfo,
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
            ResultHeader(searchInfo = searchInfo, onClickBackButton = onBackToSearchScreen)

            if (professors.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No courses found",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = APP_DEFAULT_FONT,
                        color = NoCourseFoundTextColor,
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()){
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
                    FadeoutCover()
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
            Text(
                text = "Fetching terms...",
                fontFamily = APP_DEFAULT_FONT,
                color = FetchingTermsTextColor,
                modifier = Modifier.padding(top = 30.dp)
            )
        }
    }
}