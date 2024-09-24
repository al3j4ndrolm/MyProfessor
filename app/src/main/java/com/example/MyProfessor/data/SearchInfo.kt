package com.example.MyProfessor.data

data class SearchInfo(
    var department:String = "",
    var courseCode:String = "",
    var term: TermData? = null,
){

    fun isReady() = department.isNotBlank() && courseCode.isNotBlank() && term != null
}
