package com.example.fittrack.util

import android.content.Context
import android.content.Intent
import com.example.fittrack.data.Goals
import java.time.LocalDate
import java.util.Locale

object ShareTools {
    fun report(date: LocalDate, daily: DailySummary, goals: Goals): String = buildString {
        appendLine("FitTrack daily report — $date")
        appendLine("Workouts: ${daily.workoutsCount} (${daily.activeMinutes}/${goals.activityMinutesTarget} min)")
        appendLine("Food: ${daily.foodCalories}/${goals.caloriesTarget} kcal")
        appendLine("Workout calories: ${daily.burnedCalories} kcal")
        appendLine("Protein: ${"%.1f".format(Locale.US, daily.proteinGrams)} g")
        appendLine("Sleep: ${"%.1f".format(Locale.US, daily.sleepHours)} / ${goals.sleepHoursTarget} h")
        appendLine("Water: ${daily.waterMl}/${goals.waterMlTarget} ml")
        appendLine("Logged food minus exercise: ${daily.loggedNetCalories} kcal")
        append("Generated with FitTrack")
    }

    fun share(context: Context, report: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "FitTrack fitness report")
            putExtra(Intent.EXTRA_TEXT, report)
        }
        context.startActivity(Intent.createChooser(intent, "Share fitness report"))
    }
}
