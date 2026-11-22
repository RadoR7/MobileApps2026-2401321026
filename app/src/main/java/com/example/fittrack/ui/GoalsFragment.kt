package com.example.fittrack.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.fittrack.data.Goals
import com.example.fittrack.databinding.FragmentGoalsBinding
import com.example.fittrack.util.Validation
import com.example.fittrack.util.toast
import com.example.fittrack.util.value

class GoalsFragment : Fragment() {
    private val vm by sharedFitViewModel()
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.goals.observe(viewLifecycleOwner) { current ->
            val goals = current ?: Goals()
            binding.goalCalories.setText(goals.caloriesTarget.toString())
            binding.goalActivity.setText(goals.activityMinutesTarget.toString())
            binding.goalSleep.setText(goals.sleepHoursTarget.toString())
            binding.goalWater.setText(goals.waterMlTarget.toString())
        }
        binding.saveGoals.setOnClickListener {
            val calories = binding.goalCalories.value().toIntOrNull()
            val activity = binding.goalActivity.value().toIntOrNull()
            val sleep = binding.goalSleep.value().toDoubleOrNull()
            val water = binding.goalWater.value().toIntOrNull()
            val error = Validation.goals(calories, activity, sleep, water)
            if (error != null) toast(error) else {
                vm.saveGoals(Goals(caloriesTarget = calories!!, activityMinutesTarget = activity!!,
                    sleepHoursTarget = sleep!!, waterMlTarget = water!!))
                toast("Goals saved")
            }
        }
        binding.darkMode.isChecked = requireContext().getSharedPreferences("ui", Context.MODE_PRIVATE)
            .getBoolean("dark", false)
        binding.darkMode.setOnCheckedChangeListener { _, checked ->
            requireContext().getSharedPreferences("ui", Context.MODE_PRIVATE).edit()
                .putBoolean("dark", checked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}
