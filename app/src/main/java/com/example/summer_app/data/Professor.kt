package com.example.summer_app.data


data class Professor(
    val name: String = "Vinh Nguyen",
    val all_schedules: Map<String, List<String>> = mapOf(Pair("29630", listOf("MTWR - 10:00am - 11:50am/ONLINE", "M - 10:00am - 11:50am/ONLINE"))),
    var ratingData: ProfessorRatingData? = null
)
