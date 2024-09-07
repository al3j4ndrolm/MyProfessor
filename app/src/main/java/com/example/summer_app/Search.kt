package com.example.summer_app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun searchProfessors(professorName: String ): List<Professor> {
    // State to hold professor data
    var professors by remember { mutableStateOf<List<Professor>>(emptyList()) }
    // Launch a coroutine to fetch data when the composable first loads
    LaunchedEffect(Unit) {
        professors = fetchProfessorsFromAPI(professorName)
    }

    return professors
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

suspend fun fetchProfessorsFromAPI(professorName: String): List<Professor> {
    val apiUrl = "https://af60-2603-8001-3d00-577e-25f2-f509-ab36-6b6.ngrok-free.app/api/professors?name=${professorName}%20"
    return withContext(Dispatchers.IO) {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
            val professorListType = object : TypeToken<List<Professor>>() {}.type
            Gson().fromJson(inputStream, professorListType)
        } else {
            println("Failed to fetch data from the API. Response Code: $responseCode")
            emptyList()
        }
    }
}
