package com.example.fittrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fittrack.databinding.ActivityMainBinding
import com.example.fittrack.ui.DashboardFragment
import com.example.fittrack.ui.FoodFragment
import com.example.fittrack.ui.GoalsFragment
import com.example.fittrack.ui.SleepFragment
import com.example.fittrack.ui.WorkoutFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainRoot) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, bars.top, 0, bars.bottom)
            insets
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_today -> DashboardFragment()
                R.id.nav_activity -> WorkoutFragment()
                R.id.nav_food -> FoodFragment()
                R.id.nav_sleep -> SleepFragment()
                R.id.nav_goals -> GoalsFragment()
                else -> return@setOnItemSelectedListener false
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            true
        }
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_today
        }
    }
}
