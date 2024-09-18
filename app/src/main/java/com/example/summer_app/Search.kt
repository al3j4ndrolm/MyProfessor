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

@Composable
fun searchProfessors(department: String , courseCode: String, term: String, context:Context): List<Professor> {

    var professors by remember { mutableStateOf<List<Professor>>(emptyList()) }
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

@Composable
fun getTerms(context: Context): Pair<List<String>, List<String>> {
    var terms by remember { mutableStateOf<List<String>>(emptyList()) }
    var code by remember { mutableStateOf<List<String>>(emptyList()) }



    LaunchedEffect(Unit) {
        val result: PyObject = fetchAvailableTerms(context)
        val (termTextList, termCodeList) = loadTermListFromPyObject(result)
        terms = termTextList
        code = termCodeList
    }

    return terms to code
}

suspend fun loadTermListFromPyObject(termsData: PyObject): Pair<List<String>, List<String>> {
    return withContext(Dispatchers.IO) {
        val gson = Gson()
        val termsList = mutableListOf<String>()
        val termsCodeList = mutableListOf<String>()

        for (termData in termsData.asList()) {
            val jsonObject = gson.fromJson(termData.toString(), JsonObject::class.java)

            val termText = jsonObject.get("term_text").asString
            termsList.add(termText)  // Add the term text to the list

            val termCode = jsonObject.get("term_code").asString
            termsCodeList.add(termCode)

        }
        return@withContext Pair(termsList, termsCodeList)
    }
}
