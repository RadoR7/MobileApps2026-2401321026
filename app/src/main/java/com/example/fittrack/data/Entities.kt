package com.example.fittrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val type: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val date: String,
    val notes: String = ""
)

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val mealType: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val date: String,
    val notes: String = ""
)

@Entity(tableName = "sleep_entries")
data class SleepEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: String,
    val bedTime: String,
    val wakeTime: String,
    val hours: Double,
    val quality: Int,
    val notes: String = ""
)

@Entity(tableName = "water_entries")
data class WaterEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: String,
    val amountMl: Int
)

@Entity(tableName = "goals")
data class Goals(
    @PrimaryKey val id: Int = 1,
    val caloriesTarget: Int = 2200,
    val activityMinutesTarget: Int = 45,
    val sleepHoursTarget: Double = 8.0,
    val waterMlTarget: Int = 2000
)
