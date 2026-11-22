package com.example.fittrack

import com.example.fittrack.data.Food
import com.example.fittrack.data.SleepEntry
import com.example.fittrack.data.WaterEntry
import com.example.fittrack.data.Workout
import com.example.fittrack.util.TrackerMath
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackerMathTest {
    @Test fun overnightSleep() = assertEquals(8.0, TrackerMath.sleepHours("23:00", "07:00"), 0.001)
    @Test fun daytimeSleep() = assertEquals(2.5, TrackerMath.sleepHours("13:00", "15:30"), 0.001)
    @Test fun midnightSleep() = assertEquals(1.0, TrackerMath.sleepHours("23:30", "00:30"), 0.001)
    @Test fun fullDayTimeEquality() = assertEquals(24.0, TrackerMath.sleepHours("10:00", "10:00"), 0.001)
    @Test fun progressClampsToHundred() = assertEquals(100, TrackerMath.percent(200.0, 100.0))
    @Test fun progressHandlesZeroTarget() = assertEquals(0, TrackerMath.percent(50.0, 0.0))
    @Test fun progressRounds() = assertEquals(33, TrackerMath.percent(1.0, 3.0))

    @Test fun dailyAggregatesOnlyChosenDate() {
        val date = "2026-11-21"
        val workouts = listOf(
            Workout(1, "Run", "Running", 30, 200, date),
            Workout(2, "Gym", "Strength", 60, 350, date),
            Workout(3, "Old", "Walking", 10, 50, "2026-11-20")
        )
        val food = listOf(
            Food(1, "Lunch", "Lunch", 500, 35.0, 60.0, 10.0, date),
            Food(2, "Old", "Snack", 100, 2.0, 10.0, 3.0, "2026-11-20")
        )
        val sleeps = listOf(SleepEntry(1, date, "23:00", "07:00", 8.0, 4))
        val water = listOf(WaterEntry(1, date, 250), WaterEntry(2, date, 500))
        val actual = TrackerMath.today(date, workouts, food, sleeps, water)
        assertEquals(2, actual.workoutsCount)
        assertEquals(90, actual.activeMinutes)
        assertEquals(550, actual.burnedCalories)
        assertEquals(500, actual.foodCalories)
        assertEquals(-50, actual.loggedNetCalories)
        assertEquals(35.0, actual.proteinGrams, 0.001)
        assertEquals(750, actual.waterMl)
        assertEquals(8.0, actual.sleepHours, 0.001)
        assertEquals(4, actual.sleepQuality)
    }
}
