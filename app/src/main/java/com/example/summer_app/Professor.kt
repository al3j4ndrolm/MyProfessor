package com.example.summer_app


data class Professor(
    val name: String = "unknown",
    val all_schedules: Map<String, List<String>> = mapOf(),
    val num_ratings: Int = 0,
    val difficulty: Double = 0.0,
    val overall_rating: Double = 0.0,
    val would_take_again: Double = 99.0,
)
