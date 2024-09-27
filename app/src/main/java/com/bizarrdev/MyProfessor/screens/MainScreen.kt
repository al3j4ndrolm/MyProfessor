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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.bizarrdev.MyProfessor.ui.RecentSearchSection
import com.bizarrdev.MyProfessor.ui.ResultHeader
import com.bizarrdev.MyProfessor.ui.SearchBoxGuideText
import com.bizarrdev.MyProfessor.ui.SearchButton
import com.bizarrdev.MyProfessor.ui.SearchInputTextField
import com.bizarrdev.MyProfessor.ui.SearchLoadingScene
import com.bizarrdev.MyProfessor.ui.TermButtons
import com.bizarrdev.MyProfessor.ui.theme.APP_DEFAULT_FONT
import com.bizarrdev.MyProfessor.ui.theme.ErrorMessageTextBackground
import com.bizarrdev.MyProfessor.ui.theme.FetchingTermsTextColor
import com.bizarrdev.MyProfessor.ui.theme.NoCourseFoundTextColor
import com.bizarrdev.MyProfessor.usecase.DataManager
import com.bizarrdev.MyProfessor.usecase.DataStorageManager
import com.bizarrdev.MyProfessor.usecase.parseInputString
import kotlinx.coroutines.delay
import java.util.stream.Collectors.toList

class MainScreen(val context: Context) {
    private val dataStorageManager: DataStorageManager = DataStorageManager()
    private val dataManager: DataManager = DataManager(context)
    private var professors: List<Professor> = listOf()
    private var searchInput: String = ""
    private var searchInfo: SearchInfo = SearchInfo()
    private var errorMessage: String = ""

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun Launch() {
        dataManager.loadMostRecentSearch(dataStorageManager.readRecentSearchData(context))

        var screenSection: ScreenSection by remember { mutableStateOf(SEARCH_INPUT) }
        var showMessage by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize().background(Color.White)){
            when (screenSection) {
                SEARCH_INPUT -> SearchInputScreen(
                    onEnterLoadingScreen = { screenSection = SEARCH_LOADING },
                    onPopErrorMessage = {
                        errorMessage = it
                        showMessage = true
                    }
                )

                SEARCH_LOADING -> SearchLoadingScreen(
                    onEnterResultScreen = { screenSection = SEARCH_RESULT },
                    onBackToSearchScreen = {
                        searchInput = ""
                        screenSection = SEARCH_INPUT
                    }
                )

                SEARCH_RESULT -> ResultScreen(
                    onBackToSearchScreen = {
                        searchInput = ""
                        screenSection = SEARCH_INPUT
                    }
                )
            }
            if (showMessage) {
                ErrorMessage(errorMessage) {
                    showMessage = false
                    errorMessage = ""
                }
            }
        }
    }

    @Composable
    fun ErrorMessage(errorMessage: String, onTimeout: () -> Unit){
        LaunchedEffect(Unit) {
            delay(2000) // Wait for 2 seconds
            onTimeout() // Hide the text field after 2 seconds
        }

        Box(modifier = Modifier.fillMaxSize().padding(bottom = 40.dp),
            contentAlignment = Alignment.Center){
            // Text field or any content you want to show temporarily
            Text(
                text = errorMessage,
                modifier = Modifier.background(ErrorMessageTextBackground, shape = RoundedCornerShape(4.dp)).padding(horizontal = 16.dp, vertical = 4.dp),
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun SearchInputScreen(
        onEnterLoadingScreen: () -> Unit,
        onPopErrorMessage: (String) -> Unit
    ) {
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
                        if (searchInfo.term == null){
                            onPopErrorMessage("Please wait for loading terms.")
                        } else if (parsedInput == null) {
                            onPopErrorMessage("Please use correct course code as example.")
                        } else {
                            val (searchInput1, searchInput2) = parsedInput
                            searchInfo.department = searchInput1
                            searchInfo.courseCode = searchInput2
                            dataManager.updateMostRecentSearch(searchInfo)
                            dataStorageManager.saveRecentSearchData(context, dataManager.recentSearch)
                            onEnterLoadingScreen()
                        }
                    },
                    enabled = isReadyToSearch
                )
            }

            TermOptionsRow(
                updateChosenTerm = {
                    searchInfo.term = it
                    isReadyToSearch = searchInput.isNotBlank()
                },
            )

            RecentSearchSection(
                recentSearch = dataManager.recentSearch,
                onClick = {
                    searchInfo = it.copy()
                    dataManager.updateMostRecentSearch(searchInfo)
                    dataStorageManager.saveRecentSearchData(context, dataManager.recentSearch)
                    onEnterLoadingScreen()
                })
        }
    }

    @Composable
    fun SearchLoadingScreen(
        onEnterResultScreen: () -> Unit,
        onBackToSearchScreen: () -> Unit
    ) {
        LaunchedEffect(Unit) {
            dataManager.startSearchingProfessors(
                searchInfo = searchInfo,
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
    private fun TermOptionsRow(updateChosenTerm: (TermData) -> Unit) {
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
                text = "Getting terms from school...",
                fontFamily = APP_DEFAULT_FONT,
                color = FetchingTermsTextColor,
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}