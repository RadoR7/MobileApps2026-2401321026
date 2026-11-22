package com.example.fittrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fittrack.data.FitRepository
import com.example.fittrack.data.Food
import com.example.fittrack.data.Goals
import com.example.fittrack.data.SleepEntry
import com.example.fittrack.data.WaterEntry
import com.example.fittrack.data.Workout
import kotlinx.coroutines.launch

class FitViewModel(private val repository: FitRepository) : ViewModel() {
    val workouts = repository.workouts.asLiveData()
    val foods = repository.foods.asLiveData()
    val sleep = repository.sleep.asLiveData()
    val water = repository.water.asLiveData()
    val goals = repository.goals.asLiveData()

    fun saveWorkout(item: Workout) = viewModelScope.launch { repository.saveWorkout(item) }
    fun deleteWorkout(item: Workout) = viewModelScope.launch { repository.deleteWorkout(item) }
    fun saveFood(item: Food) = viewModelScope.launch { repository.saveFood(item) }
    fun deleteFood(item: Food) = viewModelScope.launch { repository.deleteFood(item) }
    fun saveSleep(item: SleepEntry) = viewModelScope.launch { repository.saveSleep(item) }
    fun deleteSleep(item: SleepEntry) = viewModelScope.launch { repository.deleteSleep(item) }
    fun saveWater(item: WaterEntry) = viewModelScope.launch { repository.saveWater(item) }
    fun deleteWater(item: WaterEntry) = viewModelScope.launch { repository.deleteWater(item) }
    fun saveGoals(item: Goals) = viewModelScope.launch { repository.saveGoals(item) }

    class Factory(private val repository: FitRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FitViewModel::class.java)) {
                return FitViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}
