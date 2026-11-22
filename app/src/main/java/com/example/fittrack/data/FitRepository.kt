package com.example.fittrack.data

// The repository is the only layer that talks to Room DAO methods.
// MainActivity/fragments never access SQLite directly.
class FitRepository(private val dao: FitDao) {
    val workouts = dao.observeWorkouts()
    val foods = dao.observeFoods()
    val sleep = dao.observeSleep()
    val water = dao.observeWater()
    val goals = dao.observeGoals()

    suspend fun saveWorkout(value: Workout) = if (value.id == 0L) {
        dao.insertWorkout(value)
    } else {
        dao.updateWorkout(value)
    }
    suspend fun deleteWorkout(value: Workout) = dao.deleteWorkout(value)

    suspend fun saveFood(value: Food) = if (value.id == 0L) {
        dao.insertFood(value)
    } else {
        dao.updateFood(value)
    }
    suspend fun deleteFood(value: Food) = dao.deleteFood(value)

    suspend fun saveSleep(value: SleepEntry) = if (value.id == 0L) {
        dao.insertSleep(value)
    } else {
        dao.updateSleep(value)
    }
    suspend fun deleteSleep(value: SleepEntry) = dao.deleteSleep(value)

    suspend fun saveWater(value: WaterEntry) = if (value.id == 0L) {
        dao.insertWater(value)
    } else {
        dao.updateWater(value)
    }
    suspend fun deleteWater(value: WaterEntry) = dao.deleteWater(value)

    suspend fun saveGoals(value: Goals) = dao.saveGoals(value)
}
