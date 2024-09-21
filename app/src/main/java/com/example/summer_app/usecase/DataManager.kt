package com.example.summer_app.usecase

import android.content.Context
import com.example.summer_app.data.Professor
import com.example.summer_app.data.ProfessorRatingData
import com.example.summer_app.data.TermData

class DataManager {
    val availableTerms: MutableList<TermData> = mutableListOf()

    private var isSearchProfessorsPending = false

    fun startSearchingProfessors(
        context: Context,
        department: String,
        courseCode: String,
        term: String,
        onResultReceived: (List<Professor>) -> Unit,
    ) {
        isSearchProfessorsPending = true

        fetchProfessors(
            context = context,
            department = department,
            courseCode = courseCode,
            term = term,
            onResultReceived = {
                if (isSearchProfessorsPending){
                    onResultReceived(it)
                }
            })
    }

    fun stopSearchingProfessors() {
        isSearchProfessorsPending = false
    }

    fun searchAvailableTerms(context: Context, onResultReceived: (List<TermData>) -> Unit){
        if (availableTerms.isNotEmpty()) {
            onResultReceived(availableTerms)
            return
        }

        val startTimestamp = System.currentTimeMillis()

        fetchAvailableTerms(
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

    fun searchProfessorRatings(
        context: Context,
        professorName: String,
        onResultReceived: (ProfessorRatingData) -> Unit
    ) {

    }

    companion object {
         fun getDurationInSeconds(startTimestamp: Long) =
            (System.currentTimeMillis() - startTimestamp).toDouble() / 1000.0
    }
}