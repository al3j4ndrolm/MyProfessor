package com.bizarrdev.MyProfessor.usecase

import android.content.Context
import android.util.Log
import com.bizarrdev.MyProfessor.data.Professor
import com.bizarrdev.MyProfessor.data.ProfessorRatingData
import com.bizarrdev.MyProfessor.data.SearchInfo
import com.bizarrdev.MyProfessor.data.TermData
import com.bizarrdev.MyProfessor.ui.theme.MOST_RECENT_SEARCH_LIMIT

class DataManager(
    val context: Context
) {
    val recentSearch: MutableList<SearchInfo> = mutableListOf()
    val availableTerms: MutableList<TermData> = mutableListOf()

    private val cachedProfessors :MutableMap<SearchInfo, List<Professor>> = mutableMapOf()
    private val cachedProfessorRatingData:MutableMap<String, ProfessorRatingData> = mutableMapOf()
    private var isSearchProfessorsPending = false

    fun loadMostRecentSearch(searchInfos : List<SearchInfo>){
        Log.d("DataManager", "Loading recent search info from storage.")
        recentSearch.clear()
        recentSearch.addAll(searchInfos)
    }

    fun updateMostRecentSearch(searchInfo: SearchInfo){
        if (recentSearch.contains(searchInfo)){
            recentSearch.remove(searchInfo)
        }
        if (recentSearch.size == MOST_RECENT_SEARCH_LIMIT){
            recentSearch.removeLast()
        }
        recentSearch.add(0, searchInfo.copy())
    }

    fun startSearchingProfessors(
        searchInfo: SearchInfo,
        onResultReceived: (List<Professor>) -> Unit,
    ) {
        if (cachedProfessors.containsKey(searchInfo)){
            cachedProfessors[searchInfo]?.let { onResultReceived(it) }
            return
        }

        val startTime = System.currentTimeMillis()
        isSearchProfessorsPending = true

        searchProfessors(
            context = context,
            department = searchInfo.department,
            courseCode = searchInfo.courseCode,
            term = searchInfo.term!!.termCode,
            onResultReceived = {
                if (isSearchProfessorsPending){
                    isSearchProfessorsPending = false
                    log("Completed schedules fetching in ${getLatency(startTime)} seconds.")
                    val professors = it.data as List<Professor>
                    cachedProfessors[searchInfo] = professors
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

        val startTime = System.currentTimeMillis()

        searchAvailableTerms(
            context = context,
            onResultReceived = {
                log("Completed terms fetching in ${getLatency(startTime)} seconds.")
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
                if (it.errorMessage.isBlank()){
                    val ratingData = it.data as ProfessorRatingData
                    cachedProfessorRatingData[professorName] = ratingData
                    onResultReceived(ratingData)
                }
            })
    }

    companion object {
        fun getLatency(startTimestamp: Long) =
            (System.currentTimeMillis() - startTimestamp).toDouble() / 1000.0

        fun log(message: String) {
            Log.d("DataManager", message)
        }
    }
}