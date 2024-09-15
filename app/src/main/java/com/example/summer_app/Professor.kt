package com.example.summer_app

import org.jetbrains.annotations.Async.Schedule

data class Professor(
    val name: String = "unknown",
    val schedule: List<String> = listOf(),
    val overall_rating: Double = 0.0,
    val difficulty: Double = 0.0,
    val would_take_again: Double = 99.0,
    val num_ratings: Int = 0,
)
