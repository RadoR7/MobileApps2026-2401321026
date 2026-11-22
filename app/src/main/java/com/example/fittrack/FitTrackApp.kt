package com.example.fittrack

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.example.fittrack.data.FitDatabase
import com.example.fittrack.data.FitRepository

class FitTrackApp : Application() {
    val repository: FitRepository by lazy {
        val database = Room.databaseBuilder(
            applicationContext,
            FitDatabase::class.java,
            "fittrack_database"
        ).build()
        FitRepository(database.fitDao())
    }

    override fun onCreate() {
        super.onCreate()
        val dark = getSharedPreferences("ui", MODE_PRIVATE).getBoolean("dark", false)
        AppCompatDelegate.setDefaultNightMode(
            if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
