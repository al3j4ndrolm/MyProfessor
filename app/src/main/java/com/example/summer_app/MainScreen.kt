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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import java.time.LocalDate

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
        var showSearchResult by remember { mutableStateOf(false)
        }
        val currentDate = LocalDate.now()
        val (currentTerm, nextTerm) = getCurrentAndNextTerm(currentDate)
        var choosenTerm by remember { mutableStateOf("testing") }


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
                            showSearchResult = true
                        })
                    }
                    termButtons(
                        currentTerm = currentTerm,
                        nextTerm = nextTerm,
                        onClickCurrentTerm = {
                            choosenTerm = currentTerm
                        },
                        onClickNextTerm = {
                            choosenTerm = nextTerm
                        }
                    )
                }
            }


            if (showSearchResult) {
                val (department, code) = parseCourseInfo(className)
                val professors = searchProfessors(department.uppercase(), code.uppercase(), "F2024", context)
//                val professors = listOf(Professor())

                if (professors.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(R.drawable.searching)
                                .decoderFactory { result, options, _ ->
                                    ImageDecoderDecoder(result.source, options)
                                }
                                .build(),
                            contentDescription = "Loading GIF"
                        )
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
                            professorDisplayUI.searchResultHeader("${department.uppercase()} ${code.uppercase()}", "$choosenTerm ${currentDate.year}")
                            Text(text = String.format("latency: %s seconds", (endTime - startTime).toDouble()/1000.0),
                                fontSize = 10.sp)

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            ) {
                                for (professor in professors) {
                                    professorDisplayUI.professorInformationDisplay(professor)
                                }
                                BackSearchResultButton(onClick = {
                                    showSearchResult = false
                                    className = ""
                                })
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
    fun SearchButton(onClick: () -> Unit) {
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
                modifier = Modifier.size(35.dp)
            )
        }
    }

    @Composable
    fun BackSearchResultButton(onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text("Back")
        }
    }

    fun getCurrentAndNextTerm(date: LocalDate): Pair<String, String> {
        val year = date.year

        val fallStart = LocalDate.of(year, 9, 25)
        val winterStart = LocalDate.of(year, 1, 8)
        val springStart = LocalDate.of(year, 4, 8)
        val summerStart = LocalDate.of(year, 7, 1)

        return when {
            date.isAfter(fallStart.minusDays(1)) || date.isBefore(winterStart) -> "Fall" to "Winter"
            date.isAfter(winterStart.minusDays(1)) && date.isBefore(springStart) -> "Winter" to "Spring"
            date.isAfter(springStart.minusDays(1)) && date.isBefore(summerStart) -> "Spring" to "Summer"
            date.isAfter(summerStart.minusDays(1)) && date.isBefore(fallStart) -> "Summer" to "Fall"
            else -> "Unknown Term" to "Unknown Term"
        }
    }

    @Composable
    fun termButtons(onClickCurrentTerm: () -> Unit, onClickNextTerm: () -> Unit, currentTerm: String, nextTerm: String){

        Row {
            Button(onClick = { onClickCurrentTerm() }) {
                Text(text = currentTerm)
            }
            Button(onClick = { onClickNextTerm() }) {
                Text(text = nextTerm)
            }
        }
    }
    fun getTerm(term: String): String {
        val currentYear = LocalDate.now().year
        val termMap = mapOf(
            "Spring" to "",
            "Summer" to "M",
            "Fall" to "F",
            "Winter" to "W"
        )
        return termMap[term] + currentYear.toString()
    }
}