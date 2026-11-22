package com.example.fittrack.util

import com.example.fittrack.data.Food
import com.example.fittrack.data.SleepEntry
import com.example.fittrack.data.WaterEntry
import com.example.fittrack.data.Workout
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class DailySummary(
    val foodCalories: Int,
    val burnedCalories: Int,
    val activeMinutes: Int,
    val workoutsCount: Int,
    val proteinGrams: Double,
    val sleepHours: Double,
    val sleepQuality: Int?,
    val waterMl: Int
) {
    // Logged food minus exercise only; NOT total daily energy expenditure.
    val loggedNetCalories: Int get() = foodCalories - burnedCalories
}

object TrackerMath {
    fun sleepHours(bed: String, wake: String): Double {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val start = LocalTime.parse(bed, formatter)
        val end = LocalTime.parse(wake, formatter)
        var minutes = Duration.between(start, end).toMinutes()
        if (minutes <= 0L) minutes += 24 * 60
        return minutes / 60.0
    }

    fun percent(current: Double, target: Double): Int =
        if (target <= 0.0) 0 else ((current / target) * 100.0).roundToInt().coerceIn(0, 100)

    fun today(
        date: String,
        workouts: List<Workout>,
        foods: List<Food>,
        sleeps: List<SleepEntry>,
        water: List<WaterEntry>
    ): DailySummary {
        val selectedWorkouts = workouts.filter { it.date == date }
        val selectedFoods = foods.filter { it.date == date }
        val selectedSleep = sleeps.filter { it.date == date }.maxByOrNull { it.id }
        return DailySummary(
            foodCalories = selectedFoods.sumOf { it.calories },
            burnedCalories = selectedWorkouts.sumOf { it.caloriesBurned },
            activeMinutes = selectedWorkouts.sumOf { it.durationMinutes },
            workoutsCount = selectedWorkouts.size,
            proteinGrams = selectedFoods.sumOf { it.protein },
            sleepHours = selectedSleep?.hours ?: 0.0,
            sleepQuality = selectedSleep?.quality,
            waterMl = water.filter { it.date == date }.sumOf { it.amountMl }
        )
    }
}
