package com.example.summer_app.usecase

import android.content.Context
import com.example.summer_app.data.Professor
import com.example.summer_app.data.ProfessorRatingData
import com.example.summer_app.data.TermData

class DataManager {
    val availableTerms: MutableList<TermData> = mutableListOf()

    private val cachedProfessorRatingData:MutableMap<String, ProfessorRatingData> = mutableMapOf()
    private var isSearchProfessorsPending = false

    fun startSearchingProfessors(
        context: Context,
        department: String,
        courseCode: String,
        term: String,
        onResultReceived: (List<Professor>) -> Unit,
    ) {
        val startTimestamp = System.currentTimeMillis()
        isSearchProfessorsPending = true

        searchProfessors(
            context = context,
            department = department,
            courseCode = courseCode,
            term = term,
            onResultReceived = {
                if (isSearchProfessorsPending){
                    isSearchProfessorsPending = false
                    println(
                        String.format(
                            "Completed professors fetching in %s seconds.",
                            getDurationInSeconds(startTimestamp)
                        )
                    )
                    val professors = it.data as List<Professor>
                    if (it.errorMessage.isBlank()){

                    }
                    onResultReceived(professors)
                }
            })
    }

    fun stopSearchingProfessors() {
        isSearchProfessorsPending = false
    }

    fun fetchAvailableTerms(context: Context, onResultReceived: (List<TermData>) -> Unit){
        if (availableTerms.isNotEmpty()) {
            onResultReceived(availableTerms)
            return
        }

        val startTimestamp = System.currentTimeMillis()

        searchAvailableTerms(
            context = context,
            onResultReceived = {
                println(
                    String.format(
                        "Completed terms fetching in %s seconds.",
                        getDurationInSeconds(startTimestamp)
                    )
                )
                availableTerms.addAll(it)
                onResultReceived(it)
            })
    }

    fun fetchProfessorRatings(
        context: Context,
        professorName: String,
        department: String,
        onResultReceived: (ProfessorRatingData) -> Unit
    ) {
        if (cachedProfessorRatingData.containsKey(professorName)){
            cachedProfessorRatingData[professorName]?.let { onResultReceived(it) }
            return
        }

        searchProfessorRatings(
            context = context,
            professorName = professorName,
            department = department,
            onResultReceived = {
                val ratingData = it.data as ProfessorRatingData
                if (it.errorMessage.isBlank()){
                    cachedProfessorRatingData[professorName] = ratingData
                }
                onResultReceived(ratingData)
            })
    }

    companion object {
         fun getDurationInSeconds(startTimestamp: Long) =
            (System.currentTimeMillis() - startTimestamp).toDouble() / 1000.0
    }
}