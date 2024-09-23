package com.example.summer_app.usecase

fun parseInputString(searchInput: String): Pair<String, String>? {
    val input = searchInput.trimEnd()

    val index = input.indexOfFirst { it.isDigit() }
    if (index > 1){
        val department = input.substring(0, index).uppercase()
        val code = input.substring(index).uppercase()
        return Pair(department, code)
    }
    return null
}