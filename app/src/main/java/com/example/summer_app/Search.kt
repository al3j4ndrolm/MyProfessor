package com.example.summer_app

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.chaquo.python.PyObject
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun searchProfessors(department: String , courseCode: String, term: String, context:Context): List<Professor> {

    var professors by remember { mutableStateOf<List<Professor>>(emptyList()) }
    // Launch a coroutine to fetch data when the composable first loads
    LaunchedEffect(Unit) {
        val result: PyObject = callPythonGetProfessorsData(
            context = context,
            department = department,
            courseCode = courseCode,
            term = term
        )
        professors = loadProfessorListFromPyObject(result)
    }

    return professors
}

suspend fun loadProfessorListFromPyObject(professorsData: PyObject): List<Professor> {
    return withContext(Dispatchers.IO) {
        val gson = Gson()
        val professorsList = mutableListOf<Professor>()

        for (professorData in professorsData.asList()) {
            professorsList.add(gson.fromJson(professorData.toString(), Professor::class.java))
        }
        return@withContext professorsList
    }
}
