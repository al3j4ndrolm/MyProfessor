package com.bizarrdev.MyProfessor

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bizarrdev.MyProfessor.data.SearchInfo
import com.bizarrdev.MyProfessor.data.TermData
import com.bizarrdev.MyProfessor.screens.MainScreen
import com.bizarrdev.MyProfessor.ui.FadeoutCover
import com.bizarrdev.MyProfessor.ui.RecentSearchRow
import com.bizarrdev.MyProfessor.ui.ResultHeader
import com.bizarrdev.MyProfessor.ui.theme.MyProfessorTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyProfessorTheme {
                MainScreen().Launch(this.applicationContext)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyProfessorTheme {
//        DashboardHeadedAndSchoolLogo()
        val searchInfo = SearchInfo(
            department = "MATH",
            courseCode = "1A",
            term = TermData(termCode = "", termText = "Fall 2024")
        )
        ResultHeader(searchInfo, onClickBackButton = {})
//        Box(modifier = Modifier.fillMaxSize().background(Color.Red)){
//            FadeoutCover()
//        }
//        RecentSearchRow(listOf(
//            SearchInfo("MATH","1A"),
//            SearchInfo("MATH","1A"),
//            SearchInfo("MATH","1A")))
    }
}
