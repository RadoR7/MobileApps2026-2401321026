package com.example.fittrack.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Workout::class, Food::class, SleepEntry::class, WaterEntry::class, Goals::class],
    version = 1,
    exportSchema = false
)
abstract class FitDatabase : RoomDatabase() {
    abstract fun fitDao(): FitDao
}
