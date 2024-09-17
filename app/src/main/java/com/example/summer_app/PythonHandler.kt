package com.example.summer_app

import android.content.Context
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

fun callPythonGetProfessorsData(context: Context, department: String, courseCode:String, term: String): PyObject {
    val pyScript = getPythonScript(context=context, scriptName = "ProfessorsFetcher")

    // Call the function from the Python script
    return pyScript.callAttr("get_professors_data", department, courseCode, term)
}

fun fetchAvailableTerms(context: Context): PyObject {
    val pyScript = getPythonScript(context=context, scriptName = "ProfessorsFetcher")

    // Call the function from the Python script
    return pyScript.callAttr("get_terms")
}

fun getPythonScript(context: Context, scriptName:String) : PyObject {
    if (!Python.isStarted()) {
        Python.start(AndroidPlatform(context))
    }
    // Get the Python instance
    val python = Python.getInstance()
    // Get the Python script you want to run
    return python.getModule(scriptName)
}