package com.example.fittrack.util

import java.time.LocalDate

object Validation {
    fun validDate(value: String): Boolean = try {
        LocalDate.parse(value)
        true
    } catch (_: Exception) {
        false
    }

    fun workout(title: String, type: String, duration: Int?, calories: Int?, date: String): String? = when {
        title.isBlank() -> "Enter the workout name."
        type.isBlank() -> "Enter an activity type."
        duration == null || duration !in 1..1440 -> "Duration must be between 1 and 1440 minutes."
        calories == null || calories !in 0..20000 -> "Calories must be between 0 and 20000."
        !validDate(date) -> "Select a valid date."
        else -> null
    }

    fun food(name: String, mealType: String, calories: Int?, protein: Double?,
             carbs: Double?, fat: Double?, date: String): String? = when {
        name.isBlank() -> "Enter a food name."
        mealType.isBlank() -> "Enter a meal type."
        calories == null || calories !in 0..20000 -> "Calories must be between 0 and 20000."
        protein == null || protein !in 0.0..2000.0 -> "Protein must be between 0 and 2000 g."
        carbs == null || carbs !in 0.0..2000.0 -> "Carbs must be between 0 and 2000 g."
        fat == null || fat !in 0.0..2000.0 -> "Fat must be between 0 and 2000 g."
        !validDate(date) -> "Select a valid date."
        else -> null
    }

    fun sleep(date: String, bed: String, wake: String, quality: Int?): String? {
        if (!validDate(date)) return "Select a valid wake-up date."
        val hours = try { TrackerMath.sleepHours(bed, wake) } catch (_: Exception) { return "Use HH:mm time format." }
        if (hours <= 0.0 || hours > 20.0) return "Sleep duration must be greater than 0 and at most 20 hours."
        if (quality == null || quality !in 1..5) return "Quality must be 1 to 5."
        return null
    }

    fun water(amount: Int?): String? = if (amount == null || amount !in 1..5000) {
        "Water must be between 1 and 5000 ml."
    } else null

    fun goals(calories: Int?, activity: Int?, sleep: Double?, water: Int?): String? = when {
        calories == null || calories !in 500..10000 -> "Calorie target must be 500 to 10000."
        activity == null || activity !in 1..1440 -> "Activity target must be 1 to 1440 minutes."
        sleep == null || sleep !in 1.0..16.0 -> "Sleep target must be 1 to 16 hours."
        water == null || water !in 250..10000 -> "Water target must be 250 to 10000 ml."
        else -> null
    }
}
