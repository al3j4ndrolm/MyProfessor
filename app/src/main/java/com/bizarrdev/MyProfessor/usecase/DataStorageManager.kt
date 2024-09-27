package com.bizarrdev.MyProfessor.usecase

import android.content.Context
import android.util.Log
import com.bizarrdev.MyProfessor.data.SearchInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class DataStorageManager {
    private val gson = Gson()
    private var json: String = ""

    fun saveRecentSearchData(context: Context, searchInfos: List<SearchInfo>) {
        try {
            val recentSearchRecordFile = File(context.filesDir, "recent_search.json")
            json = gson.toJson(searchInfos)
            recentSearchRecordFile.writeText(json)
            Log.d("DataStorageManager", "${searchInfos.size} recent searches saved.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DataStorageManager", "Error saving data: ${e.message}")
        }
    }
    fun readRecentSearchData(context: Context): List<SearchInfo> {
        val recentSearchRecordFile = File(context.filesDir, "recent_search.json")
        if (!recentSearchRecordFile.exists()) return mutableListOf()

        try {
            val jsonString = recentSearchRecordFile.readText()
            val type = object : TypeToken<List<SearchInfo>>() {}.type
            val searchInfos = gson.fromJson(jsonString, type) as List<SearchInfo>
            Log.d("DataStorageManager", "${searchInfos.size} recent searches loaded.")
            return searchInfos
        } catch (e: Exception){
            e.printStackTrace()
            Log.e("DataStorageManager", "Error loading data: ${e.message}")
            return listOf()
        }
    }
}