package com.example.summer_app.usecase

import android.content.Context
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.summer_app.data.Professor
import com.example.summer_app.data.ProfessorRatingData
import com.example.summer_app.data.ResponseData
import com.example.summer_app.data.TermData
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.Collectors.toList

internal fun searchProfessors(
    context: Context,
    department: String,
    courseCode: String,
    term: String,
    onResultReceived: (ResponseData) -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val pyScript = getPythonScript(context = context, scriptName = "ProfessorsFetcher")
        val result = pyScript.callAttr("get_professors_data", department, courseCode, term)
        val responseData = toResponseWithProfessorList(result)

        // Switch back to the main thread to update UI if necessary
        withContext(Dispatchers.Main) {
            onResultReceived(responseData)
        }
    }
}

internal fun searchAvailableTerms(context: Context, onResultReceived: (List<TermData>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val pyScript = getPythonScript(context = context, scriptName = "ProfessorsFetcher")
        val result = pyScript.callAttr("get_terms")
        val termDataList = toTermList(result)

        // Switch back to the main thread to update UI if necessary
        withContext(Dispatchers.Main) {
            onResultReceived(termDataList)
        }
    }
}

internal fun searchProfessorRatings(
    context: Context,
    professorName: String,
    department: String,
    onResultReceived: (ResponseData) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val pyScript = getPythonScript(context = context, scriptName = "RatingsFetcher")
        val result = pyScript.callAttr("get_ratings", professorName, department)
        val responseData = toResponseWithRatingData(result)

        // Switch back to the main thread to update UI if necessary
        withContext(Dispatchers.Main) {
            onResultReceived(responseData)
        }
    }
}

private fun getPythonScript(context: Context, scriptName: String): PyObject {
    if (!Python.isStarted()) {
        Python.start(AndroidPlatform(context))
    }
    // Get the Python instance
    val python = Python.getInstance()
    // Get the Python script you want to run
    return python.getModule(scriptName)
}

private fun toTermList(pyObject: PyObject): List<TermData> {
    val gson = Gson()
    val termDataList = mutableListOf<TermData>()

    for (termData in pyObject.asList()) {
        val jsonObject = gson.fromJson(termData.toString(), JsonObject::class.java)

        termDataList.add(
            TermData(
                termCode = jsonObject.get("term_code").asString,
                termText = jsonObject.get("term_text").asString,
            )
        )
    }
    return termDataList
}

private fun toResponseWithProfessorList(pyObject: PyObject): ResponseData {
    val gson = Gson()
//    val jsonArray = gson.fromJson(pyObject.toString(), JsonArray::class.java)
//
//    if (jsonArray.size() == 1){
//        val error = jsonArray[0]
//        val data : List<Professor> = listOf()
//        println(String.format("error: %s", error))
//        return ResponseData(data = data, errorMessage = error.asString)
//    }

    val professorsList =
        pyObject.asList().stream().map { gson.fromJson(it.toString(), Professor::class.java) }
            .collect(toList())
    return ResponseData(data = professorsList, errorMessage = "")
}

private fun toResponseWithRatingData(pyObject: PyObject): ResponseData {
    val gson = Gson()
    val jsonObject = gson.fromJson(pyObject.toString(), JsonObject::class.java)
    val error = jsonObject.get("error")
    val errorMessage = if (error != null) error.asString else ""

    return ResponseData(
        data = gson.fromJson(jsonObject, ProfessorRatingData::class.java),
        errorMessage =  errorMessage)
}