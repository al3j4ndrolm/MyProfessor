package com.example.summer_app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainScreen {

    @Composable
    fun Run() {

        Column {
            SearchScreen()
        }
    }

    @Composable
    fun TopScreenGraphic() {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "My\nProfessor")
                Text(text = "De Anza\nCollege")
            }
        }
    }

    @Composable
    fun SearchScreen() {
        var professorName by remember { mutableStateOf("") }
        var showSearchResult by remember { mutableStateOf(false) }


        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                TopScreenGraphic()

                Text("Look for professor...")
                TextField(
                    value = professorName,
                    onValueChange = { newText ->
                        professorName = newText
                    },
                    label = { Text("Enter your input") }
                )
                SearchButton(onClick = {
                    showSearchResult = true
                })


            }
            if (showSearchResult) {
                Box(modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()) {
                    Column {
                        ShowProfessors(professors = searchProfessors(professorName = professorName))
                        BackSearchResultButton(onClick = {
                            showSearchResult = false
                            professorName = ""
                        }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun SearchButton(onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text("Search")
        }
    }

    @Composable
    fun BackSearchResultButton(onClick: () -> Unit){
        Button(onClick = onClick) {
            Text("Back")
        }
    }

    @Composable
    fun ShowProfessors(professors: List<Professor>) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            if (professors.isNotEmpty()) {
                for (professor in professors){
                    Column {
                        Text("Name: ${professor.name}\nDepartment: ${professor.department}\nRating: ${professor.rating}\n\n")
                    }
                }
            } else {
                Text("Searching professors...")
            }
        }
    }
}