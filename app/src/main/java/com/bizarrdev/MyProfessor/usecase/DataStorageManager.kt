package com.bizarrdev.MyProfessor.usecase

import android.content.Context
import android.util.Log
import com.bizarrdev.MyProfessor.data.SearchInfo
import com.google.gson.Gson
import java.io.File

class DataStorageManager {

    private val gson = Gson()
    private var json: String = ""

    fun saveRecentSearchData(context: Context, searchInfos: List<SearchInfo>): String {
        try {
            val dataHolder = DataManager(searchInfos.toMutableList())
            val recentSearchRecordFile: File = File(context.filesDir, "recent_search.json")
            json = gson.toJson(dataHolder)
            recentSearchRecordFile.writeText(json)
            Log.d("DataStorageManager", "Recent search info saved: $json")
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error saving data: ${e.message}"
        }
        return json
    }
    fun readRecentSearchData(context: Context): List<SearchInfo> {
        val recentSearchRecordFile: File = File(context.filesDir, "recent_search.json")
        if (!recentSearchRecordFile.exists()) return mutableListOf()

        val jsonData = recentSearchRecordFile.readText()
        Log.d("DataStorageManager", "Data read: $jsonData")

        val dataHolder = gson.fromJson(jsonData, DataManager::class.java)
        return dataHolder.recentSearch
    }
}