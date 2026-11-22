package com.example.fittrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FitDao {
    @Query("SELECT * FROM workouts ORDER BY date DESC, id DESC")
    fun observeWorkouts(): Flow<List<Workout>>
    @Insert suspend fun insertWorkout(value: Workout)
    @Update suspend fun updateWorkout(value: Workout)
    @Delete suspend fun deleteWorkout(value: Workout)

    @Query("SELECT * FROM foods ORDER BY date DESC, id DESC")
    fun observeFoods(): Flow<List<Food>>
    @Insert suspend fun insertFood(value: Food)
    @Update suspend fun updateFood(value: Food)
    @Delete suspend fun deleteFood(value: Food)

    @Query("SELECT * FROM sleep_entries ORDER BY date DESC, id DESC")
    fun observeSleep(): Flow<List<SleepEntry>>
    @Insert suspend fun insertSleep(value: SleepEntry)
    @Update suspend fun updateSleep(value: SleepEntry)
    @Delete suspend fun deleteSleep(value: SleepEntry)

    @Query("SELECT * FROM water_entries ORDER BY date DESC, id DESC")
    fun observeWater(): Flow<List<WaterEntry>>
    @Insert suspend fun insertWater(value: WaterEntry)
    @Update suspend fun updateWater(value: WaterEntry)
    @Delete suspend fun deleteWater(value: WaterEntry)

    @Query("SELECT * FROM goals WHERE id = 1 LIMIT 1")
    fun observeGoals(): Flow<Goals?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGoals(value: Goals)
}
