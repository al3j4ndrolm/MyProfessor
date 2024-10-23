package com.bizarrdev.MyProfessor

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bizarrdev.MyProfessor.data.ScreenSection.SEARCH_LOADING
import com.bizarrdev.MyProfessor.data.TermData
import com.bizarrdev.MyProfessor.screens.MainScreen
import com.bizarrdev.MyProfessor.ui.TermButtons
import com.bizarrdev.MyProfessor.ui.theme.MyProfessorTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyProfessorTheme {
                MainScreen(this.applicationContext).Launch()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyProfessorTheme {
        TermButtons(
            termDataList = listOf(
                TermData(termCode = "W2024", termText = "Winter 2025"),
                TermData(termCode = "F2024", termText = "Fall 2024"),
            ),
            termCodeUpdaters = listOf(),
            selectedIndex = 0,
        )
//        DashboardHeadedAndSchoolLogo()
//        val searchInfo = SearchInfo(
//            department = "MATH",
//            courseCode = "1A",
//            term = TermData(termCode = "", termText = "Fall 2024")
//        )
//        ResultHeader(searchInfo, onClickBackButton = {})
////        Box(modifier = Modifier.fillMaxSize().background(Color.Red)){
////            FadeoutCover()
//////        }
//        RecentSearchRow(listOf(
//            SearchInfo("MATH","1A", TermData("F2024", "Fall 2024")),
//            SearchInfo("MATH","1A", TermData("", "Summer 2024")),
//            SearchInfo("MATH","1C", TermData("", "Fall 2024"))), {})
//        ScheduleContent(mutableMapOf(
//            "27505" to listOf("MTWR - 10:00 - 2:00")
//        ))
    }
}
