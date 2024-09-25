package com.bizarrdev.MyProfessor.usecase

fun parseInputString(searchInput: String): Pair<String, String>? {
    val input = searchInput.trimEnd()

    val index = input.indexOfFirst { it.isDigit() }
    if (index > 1){
        var department = input.substring(0, index).uppercase().trimEnd()
        if (department.length > 5) {
            department = department.substring(0, 5)
        }
        val code = input.substring(index).uppercase().trimEnd()
        return Pair(department, code)
    }
    return null
}
