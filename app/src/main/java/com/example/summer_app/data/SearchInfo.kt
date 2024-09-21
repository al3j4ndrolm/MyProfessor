package com.example.summer_app.data

import com.example.summer_app.TermData

data class SearchInfo(
    var department:String = "",
    var courseCode:String = "",
    var term: TermData? = null,
){

    fun isReady() = department.isNotBlank() && courseCode.isNotBlank() && term != null
}
