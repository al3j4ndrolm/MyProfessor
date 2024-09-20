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
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.stream.Collectors.toList

@Composable
fun searchProfessors(
    department: String,
    courseCode: String,
    termCode: String,
    context: Context
): List<Professor> {
    var professors by remember { mutableStateOf<List<Professor>>(emptyList()) }

    LaunchedEffect(Unit) {
        val result: PyObject = fetchProfessors(
            context = context,
            department = department,
            courseCode = courseCode,
            term = termCode,
        )
        professors = toProfessorList(result)
    }
    return professors
}

private suspend fun toProfessorList(pyObject: PyObject): List<Professor> {
    return withContext(Dispatchers.IO) {
        val gson = Gson()

        val professorsList =
            pyObject.asList().stream().map { gson.fromJson(it.toString(), Professor::class.java) }
                .collect(toList())

        return@withContext professorsList
    }
}

@Composable
fun getProfessorRatings(professorName: String, context: Context): ProfessorRatingData {
    var professorRatingData by remember { mutableStateOf(ProfessorRatingData()) }

    LaunchedEffect(Unit) {
        val result: PyObject =
            fetchProfessorRatings(context = context, professorName = professorName)
        professorRatingData = toRatingsData(result)
    }
    return professorRatingData
}

private suspend fun toRatingsData(pyObject: PyObject): ProfessorRatingData {
    return withContext(Dispatchers.IO) {
        val gson = Gson()
        return@withContext gson.fromJson(pyObject.toString(), ProfessorRatingData::class.java)
    }
}
